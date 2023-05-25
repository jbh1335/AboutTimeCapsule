package com.timecapsule.capsuleservice.service;

import com.timecapsule.capsuleservice.api.request.*;
import com.timecapsule.capsuleservice.api.response.*;
import com.timecapsule.capsuleservice.db.entity.*;
import com.timecapsule.capsuleservice.db.repository.*;
import com.timecapsule.capsuleservice.dto.*;
import com.timecapsule.capsuleservice.exception.CustomException;
import com.timecapsule.capsuleservice.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service("capsuleService")
@RequiredArgsConstructor
public class CapsuleServiceImpl implements CapsuleService {
    @Value("${kakao.localMap.key}")
    private String key;
    private final RedisService redisService;
    private final DistanceService distanceService;
    private final FcmService fcmService;
    private final CapsuleRepository capsuleRepository;
    private final MemberRepository memberRepository;
    private final CapsuleMemberRepository capsuleMemberRepository;
    private final MemoryOpenMemberRepository memoryOpenMemberRepository;
    private final MemoryRepository memoryRepository;
    private final CommentRepository commentRepository;

    @Override
    public SuccessRes<Integer> registCapsule(CapsuleRegistReq capsuleRegistReq) {
        String address = capsuleRegistReq.getAddress();
        String buildingName = getKakaoAddress(String.valueOf(capsuleRegistReq.getLongitude()), String.valueOf(capsuleRegistReq.getLatitude()));

        if(buildingName.equals("")) {
            String newAddress = address.substring(0, 4);
            address = newAddress.equals("대한민국") ? address.substring(5, address.length()) : address;
        } else {
            address = buildingName;
        }

        Capsule capsule = Capsule.builder()
                .title(capsuleRegistReq.getTitle())
                .rangeType(capsuleRegistReq.getRangeType())
                .isGroup(capsuleRegistReq.isGroup())
                .latitude(capsuleRegistReq.getLatitude())
                .longitude(capsuleRegistReq.getLongitude())
                .address(address)
                .build();

        Capsule newCapsule = capsuleRepository.save(capsule);

        int count = 0;
        Member sender = new Member();
        for(Integer id : capsuleRegistReq.getMemberIdList()) {
            Optional<Member> oMember = memberRepository.findById(id);
            Member member = oMember.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

            if(count == 0) sender = member;
            capsuleMemberRepository.save(CapsuleMember.builder().member(member).capsule(newCapsule).build());

            count++;
            if(count >= 2) fcmService.groupCapsuleInviteNotification(newCapsule, member, sender);
        }

        return new SuccessRes<>(true, "캡슐 등록을 완료했습니다.", newCapsule.getId());
    }

    private String getKakaoAddress(String longitude, String latitude) {
        String url = "https://dapi.kakao.com/v2/local/geo/coord2address.json?x="+longitude+"&y="+latitude;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "KakaoAK " + key);

            MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
            parameters.add("x", longitude);
            parameters.add("y", latitude);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity(headers), String.class);

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result.getBody());

            JSONObject meta = (JSONObject) jsonObject.get("meta");
            long totalCount = (long) meta.get("total_count");
            if(totalCount <= 0) return "";

            JSONArray documents = (JSONArray) jsonObject.get("documents");
            JSONObject address = (JSONObject) documents.get(0);
            JSONObject roadAddress = (JSONObject) address.get("road_address");
            if(roadAddress == null) return "";

            String buildingName = (String) roadAddress.get("building_name");
            return buildingName;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SuccessRes<CapsuleListRes> getMyCapsule(int memberId) {
        Optional<Member> oMember = memberRepository.findById(memberId);
        Member member = oMember.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        List<UnopenedCapsuleDto> unopenedCapsuleDtoList = new ArrayList<>();
        List<OpenedCapsuleDto> openedCapsuleDtoList = new ArrayList<>();
        List<MapInfoDto> mapInfoDtoList = new ArrayList<>();

        CapsuleDto capsuleDto = getCapsuleList(memberId, member, "my", unopenedCapsuleDtoList, openedCapsuleDtoList, mapInfoDtoList, null);
        CapsuleListRes capsuleListRes = CapsuleListRes.builder()
                .unopenedCapsuleDtoList(capsuleDto.getUnopenedCapsuleDtoList())
                .openedCapsuleDtoList(capsuleDto.getOpenedCapsuleDtoList())
                .mapInfoDtoList(capsuleDto.getMapInfoDtoList())
                .build();
        return new SuccessRes<>(true, "나의 캡슐 목록을 조회합니다.", capsuleListRes);
    }

    @Override
    public SuccessRes<CapsuleListRes> getFriendCapsule(int memberId) {
        Optional<Member> oMember = memberRepository.findById(memberId);
        Member member = oMember.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        List<UnopenedCapsuleDto> unopenedCapsuleDtoList = new ArrayList<>();
        List<OpenedCapsuleDto> openedCapsuleDtoList = new ArrayList<>();
        List<MapInfoDto> mapInfoDtoList = new ArrayList<>();
        List<Integer> capsuleIdList = new ArrayList<>();

        for(Member myFriend : friendList(member)) {
            CapsuleDto capsuleDto = getCapsuleList(memberId, myFriend, "friend", unopenedCapsuleDtoList, openedCapsuleDtoList, mapInfoDtoList, capsuleIdList);

            unopenedCapsuleDtoList = capsuleDto.getUnopenedCapsuleDtoList();
            openedCapsuleDtoList = capsuleDto.getOpenedCapsuleDtoList();
            mapInfoDtoList = capsuleDto.getMapInfoDtoList();
            capsuleIdList = capsuleDto.getCapsuleIdList();
        }

        CapsuleListRes capsuleListRes = CapsuleListRes.builder()
                .unopenedCapsuleDtoList(unopenedCapsuleDtoList)
                .openedCapsuleDtoList(openedCapsuleDtoList)
                .mapInfoDtoList(mapInfoDtoList)
                .build();

        return new SuccessRes<>(true, "친구의 캡슐 목록을 조회합니다.", capsuleListRes);
    }

    private CapsuleDto getCapsuleList(int myId, Member member, String who,
                                          List<UnopenedCapsuleDto> unopenedCapsuleDtoList,
                                          List<OpenedCapsuleDto> openedCapsuleDtoList,
                                          List<MapInfoDto> mapInfoDtoList, List<Integer> capsuleIdList) {


        for(CapsuleMember capsuleMember : member.getCapsuleMemberList()) {
            Capsule capsule = capsuleMember.getCapsule();
            int capsuleId = capsule.getId();

            if(capsule.isDeleted()) continue;
            if(who.equals("friend")) {
                if(capsuleIdList.contains(capsuleId)) continue;
                if(capsule.getRangeType().equals(RangeType.PRIVATE)) continue;
                capsuleIdList.add(capsuleId);
            }

            // 캡슐에 대한 열람 기록이 있는지 확인
            boolean isOpened = memoryOpenMemberRepository.existsByCapsuleIdAndMemberId(capsuleId, myId);
            boolean isLocked = false;

            List<Memory> memoryList = memoryRepository.findAllByCapsuleIdAndIsDeletedFalse(capsuleId);
            int memorySize = memoryList.size();
            LocalDate openDate = null;
            if(memorySize > 0) openDate = (isOpened) ? memoryList.get(0).getOpenDate() : memoryList.get(memorySize-1).getOpenDate();

            // 미열람
            if(!isOpened) {
                // 잠김 여부
                if(memorySize > 0 && openDate != null && LocalDate.now().isBefore(openDate)) isLocked = true;

                unopenedCapsuleDtoList.add(UnopenedCapsuleDto.builder()
                        .capsuleId(capsuleId)
                        .openDate(openDate)
                        .address(capsule.getAddress())
                        .isLocked(isLocked)
                        .build());
            } else {
                // 열람
                int count = memoryOpenMemberRepository.countByCapsuleIdAndMemberId(capsuleId, myId);
                boolean isAdded = false;
                if(memorySize != count) isAdded = true;

                openedCapsuleDtoList.add(OpenedCapsuleDto.builder()
                        .capsuleId(capsuleId)
                        .openDate(openDate)
                        .address(capsule.getAddress())
                        .isAdded(isAdded)
                        .build());
            }

            mapInfoDtoList.add(MapInfoDto.builder()
                    .capsuleId(capsuleId)
                    .latitude(capsule.getLatitude())
                    .longitude(capsule.getLongitude())
                    .isOpened(isOpened)
                    .isLocked(isLocked)
                    .build());
        }

        Collections.sort(unopenedCapsuleDtoList, Comparator.comparing(o -> (o.getOpenDate() == null ? LocalDate.MIN : o.getOpenDate())));
        Collections.sort(openedCapsuleDtoList, Comparator.comparing(o -> (o.getOpenDate() == null ? LocalDate.MIN : o.getOpenDate())));

        return CapsuleDto.builder()
                .unopenedCapsuleDtoList(unopenedCapsuleDtoList)
                .openedCapsuleDtoList(openedCapsuleDtoList)
                .mapInfoDtoList(mapInfoDtoList)
                .capsuleIdList(capsuleIdList)
                .build();
    }

    @Override
    public SuccessRes<OpenedCapsuleListRes> getOpenCapsule(int memberId) {
        Optional<Member> oMember = memberRepository.findById(memberId);
        Member member = oMember.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        List<OpenedCapsuleDto> openedCapsuleDtoList = new ArrayList<>();
        List<MapInfoDto> mapInfoDtoList = new ArrayList<>();
        List<Integer> capsuleIdList = new ArrayList<>();

        for(MemoryOpenMember memoryOpenMember : member.getMemoryOpenMemberList()) {
            Capsule capsule = memoryOpenMember.getCapsule();
            if(capsule.isDeleted()) continue;
            if(capsuleIdList.contains(capsule.getId())) continue;
            int capsuleId = capsule.getId();
            capsuleIdList.add(capsuleId);

            List<Memory> memoryList = memoryRepository.findAllByCapsuleIdAndIsDeletedFalse(capsuleId);
            int memorySize = memoryList.size();
            LocalDate openDate = null;
            if(memorySize > 0) openDate = memoryList.get(0).getOpenDate();

            int count = memoryOpenMemberRepository.countByCapsuleIdAndMemberId(capsuleId, memberId);
            boolean isAdded = false;
            if(memorySize != count) isAdded = true;

            openedCapsuleDtoList.add(OpenedCapsuleDto.builder()
                    .capsuleId(capsuleId)
                    .openDate(openDate)
                    .address(capsule.getAddress())
                    .isAdded(isAdded)
                    .build());

            mapInfoDtoList.add(MapInfoDto.builder()
                    .capsuleId(capsuleId)
                    .latitude(capsule.getLatitude())
                    .longitude(capsule.getLongitude())
                    .isOpened(true)
                    .isLocked(false)
                    .build());

        }

        Collections.sort(openedCapsuleDtoList, Comparator.comparing(o -> (o.getOpenDate() == null ? LocalDate.MIN : o.getOpenDate())));

        OpenedCapsuleListRes openedCapsuleListRes = OpenedCapsuleListRes.builder()
                .openedCapsuleDtoList(openedCapsuleDtoList)
                .mapInfoDtoList(mapInfoDtoList)
                .build();
        return new SuccessRes<>(true, "나의 방문 기록을 조회합니다.", openedCapsuleListRes);
    }

    @Override
    public CommonRes deleteCapsule(int capsuleId) {
        Optional<Capsule> oCapsule = capsuleRepository.findById(capsuleId);
        Capsule capsule = oCapsule.orElseThrow(() -> new CustomException(ErrorCode.CAPSULE_NOT_FOUND));

        for(Memory memory : capsule.getMemoryList()) {
            memory.getCommentList().forEach(comment -> commentRepository.save(Comment.of(comment, true)));
            memoryRepository.save(Memory.of(memory, true));
        }

        capsuleRepository.save(Capsule.of(capsule, true));

        return new CommonRes(true, "캡슐 삭제를 완료했습니다.");
    }

    @Override
    public CommonRes modifyCapsuleRange(int capsuleId, RangeType rangeType) {
        Optional<Capsule> oCapsule = capsuleRepository.findById(capsuleId);
        Capsule capsule = oCapsule.orElseThrow(() -> new CustomException(ErrorCode.CAPSULE_NOT_FOUND));

        capsuleRepository.save(Capsule.of(capsule, rangeType));
        return new CommonRes(true, "캡슐의 공개 범위를 변경했습니다.");
    }

    @Override
    public SuccessRes<List<AroundCapsuleRes>> getAroundCapsule(AroundCapsuleReq aroundCapsuleReq) {
        // 전체 공개로 설정한 모르는 사람들의 캡슐 조회 (내꺼, 친구 X)
        // 오픈 기간 지났고 내가 열람한 적 없는 주변 1km 이내에 있는 모든 캡슐 조회
        Optional<Member> oMember = memberRepository.findById(aroundCapsuleReq.getMemberId());
        Member member = oMember.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        List<AroundCapsuleRes> aroundCapsuleResList = new ArrayList<>();
        List<Capsule> aroundCapsuleList = capsuleRepository.findAroundCapsule(aroundCapsuleReq.getLongitude(), aroundCapsuleReq.getLatitude());
        for(Capsule capsule : aroundCapsuleList) {
            if(capsule.isDeleted()) continue;
            // 내 권한 X
            boolean isMember = capsuleMemberRepository.existsByCapsuleIdAndMemberId(capsule.getId(), aroundCapsuleReq.getMemberId());
            if(isMember) continue;

            if(!capsule.getRangeType().equals(RangeType.ALL)) continue;

            boolean isFriendCapsule = false;
            for(Member myFriend : friendList(member)) {
                if(capsuleMemberRepository.existsByCapsuleIdAndMemberId(capsule.getId(), myFriend.getId())) {
                    isFriendCapsule = true;
                    break;
                }
            }
            if(isFriendCapsule) continue;

            String memberNickname = capsule.getCapsuleMemberList().get(0).getMember().getNickname();
            int memberSize = capsule.getCapsuleMemberList().size() - 1;
            if(memberSize >= 1) memberNickname += (" 외 " + memberSize + "명");

            aroundCapsuleResList.add(AroundCapsuleRes.builder()
                    .capsuleId(capsule.getId())
                    .memberNickname(memberNickname)
                    .address(capsule.getAddress())
                    .build());
        }

        return new SuccessRes<>(true, "내 주변 캡슐을 조회합니다.", aroundCapsuleResList);
    }

    @Override
    public SuccessRes<LinkedHashMap<String, List<Integer>>> getAroundPopularPlace(AroundCapsuleReq aroundCapsuleReq) {
        Optional<Member> oMember = memberRepository.findById(aroundCapsuleReq.getMemberId());
        Member member = oMember.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        HashMap<String, List<Integer>> hashMap = new HashMap<>();
        List<Capsule> aroundCapsuleList = capsuleRepository.findAroundCapsule(aroundCapsuleReq.getLongitude(), aroundCapsuleReq.getLatitude());
        for(Capsule capsule : aroundCapsuleList) {
            if(capsule.isDeleted()) continue;

            boolean isMine = capsuleMemberRepository.existsByCapsuleIdAndMemberId(capsule.getId(), aroundCapsuleReq.getMemberId());
            if(!isMine && capsule.getRangeType().equals(RangeType.PRIVATE)) continue;

            boolean isFriendCapsule = false;
            for(Member myFriend : friendList(member)) {
                if(capsuleMemberRepository.existsByCapsuleIdAndMemberId(capsule.getId(), myFriend.getId())) {
                    isFriendCapsule = true;
                    break;
                }
            }
            if(!isFriendCapsule && !capsule.getRangeType().equals(RangeType.ALL)) continue;
            if(isFriendCapsule && !isMine && capsule.getRangeType().equals(RangeType.GROUP)) continue;

            List<Integer> capsuleIdList = new ArrayList<>();
            if(hashMap.containsKey(capsule.getAddress())) {
                capsuleIdList = hashMap.get(capsule.getAddress());
                capsuleIdList.add(capsule.getId());
            } else {
                capsuleIdList.add(capsule.getId());
                hashMap.put(capsule.getAddress(), capsuleIdList);
            }
        }

        List<Map.Entry<String, List<Integer>>> entryList = new ArrayList<>(hashMap.entrySet());
        entryList.sort(((o1, o2) -> o2.getValue().size() - o1.getValue().size()));

        LinkedHashMap<String, List<Integer>> popularPlaceList = new LinkedHashMap<>();
        for(Map.Entry<String, List<Integer>> entry : entryList) {
            popularPlaceList.put(entry.getKey(), entry.getValue());
        }

        return new SuccessRes<>(true, "내 주변 인기 장소를 조회합니다.", popularPlaceList);
    }

    @Override
    public SuccessRes<CapsuleListRes> getPopularPlaceCapsule(PopularPlaceReq popularPlaceReq) {
        Optional<Member> oMember = memberRepository.findById(popularPlaceReq.getMemberId());
        Member member = oMember.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        List<UnopenedCapsuleDto> unopenedCapsuleDtoList = new ArrayList<>();
        List<OpenedCapsuleDto> openedCapsuleDtoList = new ArrayList<>();
        List<MapInfoDto> mapInfoDtoList = new ArrayList<>();

        for(Integer capsuleId : popularPlaceReq.getCapsuleIdList()) {
            Optional<Capsule> oCapsule = capsuleRepository.findById(capsuleId);
            Capsule capsule = oCapsule.orElseThrow(() -> new CustomException(ErrorCode.CAPSULE_NOT_FOUND));

            if(capsule.isDeleted()) continue;

            // 캡슐에 대한 열람 기록이 있는지 확인
            boolean isOpened = memoryOpenMemberRepository.existsByCapsuleIdAndMemberId(capsuleId, popularPlaceReq.getMemberId());
            boolean isLocked = false;

            List<Memory> memoryList = memoryRepository.findAllByCapsuleIdAndIsDeletedFalse(capsuleId);
            int memorySize = memoryList.size();
            LocalDate openDate = null;
            if(memorySize > 0) openDate = (isOpened) ? memoryList.get(0).getOpenDate() : memoryList.get(memorySize-1).getOpenDate();

            // 미열람
            if(!isOpened) {
                // 잠김 여부
                if(memorySize > 0 && openDate!= null && LocalDate.now().isBefore(openDate)) isLocked = true;

                unopenedCapsuleDtoList.add(UnopenedCapsuleDto.builder()
                        .capsuleId(capsuleId)
                        .openDate(openDate)
                        .address(capsule.getAddress())
                        .isLocked(isLocked)
                        .build());
            } else {
                // 열람
                int count = memoryOpenMemberRepository.countByCapsuleIdAndMemberId(capsuleId, popularPlaceReq.getMemberId());
                boolean isAdded = false;
                if(memorySize != count) isAdded = true;

                openedCapsuleDtoList.add(OpenedCapsuleDto.builder()
                        .capsuleId(capsuleId)
                        .openDate(openDate)
                        .address(capsule.getAddress())
                        .isAdded(isAdded)
                        .build());
            }

            mapInfoDtoList.add(MapInfoDto.builder()
                    .capsuleId(capsuleId)
                    .latitude(capsule.getLatitude())
                    .longitude(capsule.getLongitude())
                    .isOpened(isOpened)
                    .isLocked(isLocked)
                    .build());
        }

        Collections.sort(unopenedCapsuleDtoList, Comparator.comparing(o -> (o.getOpenDate() == null ? LocalDate.MIN : o.getOpenDate())));
        Collections.sort(openedCapsuleDtoList, Comparator.comparing(o -> (o.getOpenDate() == null ? LocalDate.MIN : o.getOpenDate())));

        CapsuleListRes capsuleListRes = CapsuleListRes.builder()
                .unopenedCapsuleDtoList(unopenedCapsuleDtoList)
                .openedCapsuleDtoList(openedCapsuleDtoList)
                .mapInfoDtoList(mapInfoDtoList)
                .build();

        return new SuccessRes<>(true, "인기 장소의 캡슐 목록을 조회합니다.", capsuleListRes);
    }

    @Override
    public SuccessRes<List<GroupMemberRes>> getGroupMember(int capsuleId) {
        Optional<Capsule> oCapsule = capsuleRepository.findById(capsuleId);
        Capsule capsule = oCapsule.orElseThrow(() -> new CustomException(ErrorCode.CAPSULE_NOT_FOUND));

        List<GroupMemberRes> groupMemberResList = new ArrayList<>();
        for(CapsuleMember capsuleMember : capsule.getCapsuleMemberList()) {
            Member member = capsuleMember.getMember();

            groupMemberResList.add(GroupMemberRes.builder()
                    .memberId(member.getId())
                    .nickname(member.getNickname())
                    .profileImageUrl(member.getProfileImageUrl())
                    .build());
        }

        return new SuccessRes<>(true, "그룹에 해당되는 멤버 목록을 조회합니다.", groupMemberResList);
    }

    @Override
    public SuccessRes<CapsuleDetailRes> getCapsuleDetail(CapsuleDetailReq capsuleDetailReq) {
        CapsuleDetailRes capsuleDetailRes = (CapsuleDetailRes) capsuleDetail(capsuleDetailReq, "getCapsuleDetail");
        return new SuccessRes<>(true, "캡슐 클릭 시 상세 정보를 조회합니다.", capsuleDetailRes);
    }

    @Override
    public SuccessRes<MapCapsuleDetailRes> getMapCapsuleDetail(CapsuleDetailReq capsuleDetailReq) {
        MapCapsuleDetailRes mapCapsuleDetailRes = (MapCapsuleDetailRes) capsuleDetail(capsuleDetailReq, "getMapCapsuleDetail");
        return new SuccessRes<>(true, "지도에서 마커 클릭 시 캡슐의 상세 정보를 조회합니다.", mapCapsuleDetailRes);
    }

    @Override
    public SuccessRes<List<MapRes>> getMapCapsule(CapsuleDetailReq capsuleDetailReq) {
        Optional<Member> oMember = memberRepository.findById(capsuleDetailReq.getMemberId());
        Member member = oMember.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        List<MapRes> mapResList = new ArrayList<>();
        List<Capsule> aroundCapsuleList = capsuleRepository.findAroundCapsule(capsuleDetailReq.getLongitude(), capsuleDetailReq.getLatitude());

        for(Capsule capsule : aroundCapsuleList) {
            if(capsule.isDeleted()) continue;

            boolean isMine = capsuleMemberRepository.existsByCapsuleIdAndMemberId(capsule.getId(), capsuleDetailReq.getMemberId());
            if(!isMine && capsule.getRangeType().equals(RangeType.PRIVATE)) continue;
            // 친구 공개 구현하기
            // 그룹 : 전체, 그룹
            // 나 : 전체, 친구공개, 개인

            // 일반 캡슐
            // 나: 전체 o, 친구공개 o, 개인 o
            // 친구: 전체 o, 친구 o, 개인 x
            // 모르는 사람: 전체 o, 친구 x, 개인 x

            // 그룹 캡슐
            // 나: 전체 o, 그룹 o
            // 친구: 전체 o, 그룹 x
            // 모르는 사람: 전체 o, 그룹 x

            boolean isFriendCapsule = false;
            for(Member myFriend : friendList(member)) {
                if(capsuleMemberRepository.existsByCapsuleIdAndMemberId(capsule.getId(), myFriend.getId())) isFriendCapsule = true;
            }
            if(!isFriendCapsule && !capsule.getRangeType().equals(RangeType.ALL)) continue;
            if(isFriendCapsule && capsule.isGroup() && !capsule.getRangeType().equals(RangeType.ALL)) continue;

            boolean isOpened = memoryOpenMemberRepository.existsByCapsuleIdAndMemberId(capsule.getId(), capsuleDetailReq.getMemberId());
            boolean isLocked = false;

            List<Memory> memoryList = memoryRepository.findAllByCapsuleIdAndIsDeletedFalse(capsule.getId());
            int memorySize = memoryList.size();
            LocalDate openDate = null;

            if(memorySize > 0) {
                openDate = (isOpened) ? memoryList.get(0).getOpenDate() : memoryList.get(memorySize-1).getOpenDate();
                if(!isOpened && openDate != null && LocalDate.now().isBefore(openDate)) isLocked = true;
            }

            int distance = (int) distanceService.distance(capsuleDetailReq.getLatitude(), capsuleDetailReq.getLongitude(),
                    capsule.getLatitude(), capsule.getLongitude(), "meter");
            boolean isAllowedDistance = (distance <= 100);

            mapResList.add(MapRes.builder()
                    .capsuleId(capsule.getId())
                    .isLocked(isLocked)
                    .isMine(isMine)
                    .isAllowedDistance(isAllowedDistance)
                    .latitude(capsule.getLatitude())
                    .longitude(capsule.getLongitude())
                    .build());
        }
        return new SuccessRes<>(true, "1km 이내에 있는 캡슐을 조회합니다.", mapResList);
    }

    @Override
    public SuccessRes<CapsuleCountRes> getCapsuleCount(int memberId) {
        SuccessRes<CapsuleListRes> myCapsule = getMyCapsule(memberId);
        SuccessRes<CapsuleListRes> friendCapsule = getFriendCapsule(memberId);
        SuccessRes<OpenedCapsuleListRes> openCapsule = getOpenCapsule(memberId);

        boolean isNewAlarm = true;
        String newAlarm = String.valueOf(redisService.getDataValue("alarm_new", String.valueOf(memberId)));
        if(newAlarm.equals("null") || newAlarm.equals("false")) isNewAlarm = false;

        CapsuleCountRes capsuleCountRes = CapsuleCountRes.builder()
                .myCapsuleCnt(myCapsule.getData().getMapInfoDtoList().size())
                .friendCapsuleCnt(friendCapsule.getData().getMapInfoDtoList().size())
                .openCapsuleCnt(openCapsule.getData().getMapInfoDtoList().size())
                .isNewAlarm(isNewAlarm)
                .build();

        return new SuccessRes<>(true, "나의 캡슐, 친구의 캡슐, 나의 방문 기록의 개수, 새로운 알림 여부를 조회합니다.", capsuleCountRes);
    }

    @Override
    public SuccessRes<List<FriendRes>> getFriendList(int memberId) {
        Optional<Member> oMember = memberRepository.findById(memberId);
        Member member = oMember.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        List<FriendRes> friendResList = new ArrayList<>();
        friendList(member).forEach(friend -> friendResList.add(FriendRes.builder()
                .memberId(friend.getId())
                .nickname(friend.getNickname())
                .profileImageUrl(friend.getProfileImageUrl())
                .build()));

        return new SuccessRes<>(true, "나의 친구 목록을 조회합니다.", friendResList);
    }

    private Object capsuleDetail(CapsuleDetailReq capsuleDetailReq, String what) {
        Optional<Capsule> oCapsule = capsuleRepository.findById(capsuleDetailReq.getCapsuleId());
        Capsule capsule = oCapsule.orElseThrow(() -> new CustomException(ErrorCode.CAPSULE_NOT_FOUND));

        String leftTime = "";
        String memberNickname = capsule.getCapsuleMemberList().get(0).getMember().getNickname();
        int memberSize = capsule.getCapsuleMemberList().size() - 1;
        if(memberSize >= 1) memberNickname += (" 외 " + memberSize + "명");

        boolean isLocked = false;
        boolean isOpened = memoryOpenMemberRepository.existsByCapsuleIdAndMemberId(capsule.getId(), capsuleDetailReq.getMemberId());
        boolean isGroup = capsule.getCapsuleMemberList().size() > 1;

        int distance = (int) distanceService.distance(capsuleDetailReq.getLatitude(), capsuleDetailReq.getLongitude(),
                capsule.getLatitude(), capsule.getLongitude(), "meter");

        List<Memory> memoryList = memoryRepository.findAllByCapsuleIdAndIsDeletedFalse(capsuleDetailReq.getCapsuleId());
        int memorySize = memoryList.size();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        LocalDate openDate = null;

        if(memorySize > 0) {
            openDate = (isOpened) ? memoryList.get(0).getOpenDate() : memoryList.get(memorySize-1).getOpenDate();
            if(openDate != null && LocalDate.now().isBefore(openDate)) {
                isLocked = true;
                // 남은 시간 구하기
                LocalDateTime openDateTime = openDate.atStartOfDay();
                Duration duration = Duration.between(LocalDateTime.now(), openDateTime);

                leftTime += duration.toDays() + "일 " + (duration.toHours() % 24) + "시간 " + (duration.toMinutes() % 60) + "분 남음";
            }
        }

        if(what.equals("getCapsuleDetail")) {
            return CapsuleDetailRes.builder()
                    .capsuleId(capsule.getId())
                    .memberNickname(memberNickname)
                    .distance(distance)
                    .leftTime(leftTime)
                    .isLocked(isLocked)
                    .latitude(capsule.getLatitude())
                    .longitude(capsule.getLongitude())
                    .address(capsule.getAddress())
                    .build();
        }
        return MapCapsuleDetailRes.builder()
                .capsuleId(capsule.getId())
                .memberNickname(memberNickname)
                .leftTime(leftTime)
                .isLocked(isLocked)
                .isGroup(isGroup)
                .openDate(openDate != null ? openDate.format(dateTimeFormatter) : "")
                .build();
    }

    private List<Member> friendList(Member member) {
        List<Member> friendList = new ArrayList<>();
        member.getFromMemberList().forEach(friend -> {
            if(friend.isAccepted()) friendList.add(friend.getToMember());
        });

        member.getToMemberList().forEach(friend -> {
            if(friend.isAccepted()) friendList.add(friend.getFromMember());
        });

        friendList.sort(Comparator.comparing(Member::getNickname));
        return friendList;
    }

}
