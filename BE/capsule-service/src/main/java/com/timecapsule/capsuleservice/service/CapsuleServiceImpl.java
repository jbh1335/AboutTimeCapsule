package com.timecapsule.capsuleservice.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.timecapsule.capsuleservice.api.request.CapsuleRegistReq;
import com.timecapsule.capsuleservice.api.request.MemoryRegistReq;
import com.timecapsule.capsuleservice.api.response.CapsuleListRes;
import com.timecapsule.capsuleservice.api.response.SuccessRes;
import com.timecapsule.capsuleservice.db.entity.Capsule;
import com.timecapsule.capsuleservice.db.entity.CapsuleMember;
import com.timecapsule.capsuleservice.db.entity.Member;
import com.timecapsule.capsuleservice.db.repository.CapsuleMemberRepository;
import com.timecapsule.capsuleservice.db.repository.CapsuleOpenMemberRepository;
import com.timecapsule.capsuleservice.db.repository.CapsuleRepository;
import com.timecapsule.capsuleservice.db.repository.MemberRepository;
import com.timecapsule.capsuleservice.dto.MapInfoDto;
import com.timecapsule.capsuleservice.dto.OpenedCapsuleDto;
import com.timecapsule.capsuleservice.dto.UnopenedCapsuleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static com.google.common.io.Files.getFileExtension;


@Service("capsuleService")
@RequiredArgsConstructor
public class CapsuleServiceImpl implements CapsuleService {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;
    private final CapsuleRepository capsuleRepository;
    private final MemberRepository memberRepository;
    private final CapsuleMemberRepository capsuleMemberRepository;
    private final CapsuleOpenMemberRepository capsuleOpenMemberRepository;

    @Override
    public SuccessRes<Integer> registCapsule(CapsuleRegistReq capsuleRegistReq) {
        Capsule capsule = Capsule.builder()
                .title(capsuleRegistReq.getTitle())
                .isDeleted(false)
                .rangeType(capsuleRegistReq.getRangeType())
                .isLocked(true)
                .isGroup(capsuleRegistReq.isGroup())
                .latitude(capsuleRegistReq.getLatitude())
                .longitude(capsuleRegistReq.getLongitude())
                .address(capsuleRegistReq.getAddress())
                .build();

        Capsule newCapsule = capsuleRepository.save(capsule);

        for(Integer id : capsuleRegistReq.getMemberIdList()) {
            Optional<Member> oMember = memberRepository.findById(id);
            Member member = oMember.orElseThrow(() -> new IllegalArgumentException("member doesn't exist"));

            capsuleMemberRepository.save(CapsuleMember.builder().member(member).capsule(newCapsule).build());
        }

        return new SuccessRes<Integer>(true, "캡슐 등록을 완료했습니다.", newCapsule.getId());
    }

//    @Override
//    public SuccessRes<Integer> registMemory(MemoryRegistReq memoryRegistReq) {
//        List<String> list = uploadFile(memoryRegistReq.getImageList());
//        for(String str : list) {
//            System.out.println("이름: " + str);
//        }
//        return new SuccessRes<Integer>(true, "이미지 등록을 완료했습니다.", 12345);
//    }
    @Override
    public SuccessRes<Integer> registMemory(List<MultipartFile> multipartFile) {
        System.out.println("service 들어옴");
        System.out.println("크기: " + multipartFile.size());

        List<String> list = uploadFile(multipartFile);
        for(String str : list) {
            System.out.println("이름: " + str);
        }
        return new SuccessRes<Integer>(true, "이미지 등록을 완료했습니다.", 12345);
    }

    public List<String> uploadFile(List<MultipartFile> multipartFileList) {
        List<String> fileNameList = new ArrayList<>();
        multipartFileList.forEach(file -> {
            String fileName = createFileName(file.getOriginalFilename());
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength((file.getSize()));
            objectMetadata.setContentType(file.getContentType());

            try(InputStream inputStream = file.getInputStream()) {
                amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            } catch(IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
            }

            fileNameList.add(fileName);
        });

        return fileNameList;
    }

    private String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    @Override
    public SuccessRes<CapsuleListRes> getMyCapsule(int memberId) {
        Optional<Member> oMember = memberRepository.findById(memberId);
        Member member = oMember.orElseThrow(() -> new IllegalArgumentException("member doesn't exist"));

        List<UnopenedCapsuleDto> unopenedCapsuleDtoList = new ArrayList<>();
        List<OpenedCapsuleDto> openedCapsuleDtoList = new ArrayList<>();
        List<MapInfoDto> mapInfoDtoList = new ArrayList<>();

        for(CapsuleMember capsuleMember : member.getCapsuleMemberList()) {
            Capsule capsule = capsuleMember.getCapsule();
            boolean isExisted = capsuleOpenMemberRepository.existsByCapsuleIdAndMemberId(capsule.getId(), memberId);
            Date now = new Date();
            // 잠겨O 열람 O -> 불가능
            // 잠겨O 열람 X -> 위에

            // 잠겨X 열람 X -> 위에
            // 잠겨X 열람 O -> 밑에
            if(capsule.isLocked() || (!capsule.isLocked() && !isExisted)) {
                unopenedCapsuleDtoList.add(UnopenedCapsuleDto.builder()
                        .capsuleId(capsule.getId())
                        .openDate(now) // opendDate 추가
                        .address(capsule.getAddress())
                        .isLocked(true)
                        .build());
            } else {
                // 열람
                openedCapsuleDtoList.add(OpenedCapsuleDto.builder()
                        .capsuleId(capsule.getId())
                        .openDate(now) // opendDate 추가
                        .address(capsule.getAddress())
                        .build());
            }

            mapInfoDtoList.add(MapInfoDto.builder()
                    .capsuleId(capsule.getId())
                    .latitude(capsule.getLatitude())
                    .longitude(capsule.getLongitude())
                    .isOpened(isExisted)
                    .isLocked(capsule.isLocked())
                    .build());
        }

        Collections.sort(unopenedCapsuleDtoList, (o1, o2) -> o1.getOpenDate().compareTo(o2.getOpenDate()));
        Collections.sort(openedCapsuleDtoList, (o1, o2) -> o1.getOpenDate().compareTo(o2.getOpenDate()));

        CapsuleListRes capsuleListRes = CapsuleListRes.builder()
                .unopenedCapsuleDtoList(unopenedCapsuleDtoList)
                .openedCapsuleDtoList(openedCapsuleDtoList)
                .mapInfoDtoList(mapInfoDtoList)
                .build();

        return new SuccessRes<CapsuleListRes>(true, "나의 캡슐 목록을 조회합니다.", capsuleListRes);
    }
}
