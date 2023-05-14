package com.aboutcapsule.android.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aboutcapsule.android.data.capsule.AroundCapsuleDto
import com.aboutcapsule.android.data.capsule.FriendDto
import com.aboutcapsule.android.data.capsule.GetFriendListRes
import com.aboutcapsule.android.data.capsule.GetAroundCapsuleListRes
import com.aboutcapsule.android.data.capsule.GetCapsuleListRes
import com.aboutcapsule.android.data.capsule.GetGroupMemberRes
import com.aboutcapsule.android.data.capsule.GetVisitedListRes
import com.aboutcapsule.android.data.capsule.GroupMemberDto
import com.aboutcapsule.android.data.capsule.MapInfoDto
import com.aboutcapsule.android.data.capsule.OpenedCapsuleDto
import com.aboutcapsule.android.data.capsule.PostRegistCapsuleReq
import com.aboutcapsule.android.data.capsule.UnopenedCapsuleDto
import com.aboutcapsule.android.repository.CapsuleRepo
import kotlinx.coroutines.launch
import org.json.JSONObject

class CapsuleViewModel(private val repository : CapsuleRepo) : ViewModel() {
    private var TAG = "CapsuleViewModel"
    var myCapsuleList : MutableLiveData<GetCapsuleListRes> = MutableLiveData()
    var friendCapsuleList : MutableLiveData<GetCapsuleListRes> = MutableLiveData()
    var visitedCapsuleList : MutableLiveData<GetVisitedListRes> = MutableLiveData()
    var groupMemberList : MutableLiveData<GetGroupMemberRes> = MutableLiveData()
    var aroundCapsuleList : MutableLiveData<GetAroundCapsuleListRes> = MutableLiveData()
    var friendList : MutableLiveData<GetFriendListRes> = MutableLiveData()

    companion object{
        lateinit var unopenedCapsuleDtoList : MutableList<UnopenedCapsuleDto>
        lateinit var openedCapsuleDtoList : MutableList<OpenedCapsuleDto>
        lateinit var mapInfoDtoList : MutableList<MapInfoDto>
        lateinit var groupMemberDtoList : MutableList<GroupMemberDto>
        lateinit var aroundCapsuleDtoList: MutableList<AroundCapsuleDto>
        lateinit var myfriendDtoList : MutableList<FriendDto>
    }

    // 캡슐 등록
    fun addCapsule(postRegistCapsuleReq: PostRegistCapsuleReq){
        viewModelScope.launch {
            val response = repository.capsuleAdd(postRegistCapsuleReq)
            if(response.isSuccessful){
                Log.d(TAG,"addCapsule : 응답 성공 / ${response.message()}")
            }else{
                Log.d(TAG,"addCapsule : 응답 실패 / ${response.message()}" )

            }
        }
    }

    // 나의 캡슐 조회
    fun getMyCapsuleList(memberId : Int) {
        viewModelScope.launch {
            val response = repository.myCapsuleList(memberId)
            if(response.isSuccessful){
                val jsonString = response.body()?.string()
                val jsonObject = JSONObject(jsonString)
                val dataObjects = jsonObject.getJSONObject("data")

                val unopenedCapsuleDto = dataObjects.getJSONArray("unopenedCapsuleDtoList")
                unopenedCapsuleDtoList = mutableListOf()
                for(i in 0 until unopenedCapsuleDto.length()){
                    val curr = unopenedCapsuleDto.getJSONObject(i)
                    val capsuleId = curr.getInt("capsuleId")
                    val openDate = curr.getString("openDate")
                    val address = curr.getString("address")
                    val isLocked = curr.getBoolean("isLocked")
                    unopenedCapsuleDtoList.add(UnopenedCapsuleDto(capsuleId,openDate,address,isLocked))
                }

                val openedCapsuleDto = dataObjects.getJSONArray("openedCapsuleDtoList")
                openedCapsuleDtoList = mutableListOf()
                for(i in 0 until openedCapsuleDto.length()){
                    val curr = openedCapsuleDto.getJSONObject(i)
                    val capsuleId = curr.getInt("capsuleId")
                    val openDate = curr.getString("openDate")
                    val address = curr.getString("address")
                    val isAdded = curr.getBoolean("isAdded")
                    openedCapsuleDtoList.add(OpenedCapsuleDto(capsuleId,openDate,address,isAdded))
                }

                val mapInfoDto = dataObjects.getJSONArray("mapInfoDtoList")
                mapInfoDtoList = mutableListOf()
                for(i in 0 until mapInfoDto.length()){
                    val curr = mapInfoDto.getJSONObject(i)
                    val capsuleId = curr.getInt("capsuleId")
                    val latitude =curr.getDouble("latitude")
                    val longitude = curr.getDouble("longitude")
                    val isOpened = curr.getBoolean("isOpened")
                    val isLocked = curr.getBoolean("isLocked")
                    mapInfoDtoList.add(MapInfoDto(capsuleId,latitude,longitude,isOpened,isLocked))
                }

                val getCapsuleListRes = GetCapsuleListRes(unopenedCapsuleDtoList, openedCapsuleDtoList, mapInfoDtoList)

                myCapsuleList.value = getCapsuleListRes

                Log.d(TAG,"getMyCapsuleList : 응답 성공 / $dataObjects")
            }else {
                Log.d(TAG,"getCapsuleList : 응답 실패 / ${response.message()}" )
            }
        }
    }

    // 친구 캡슐 조회
    fun getFriendCapsuleList(memberId : Int ){
        viewModelScope.launch {
            val response = repository.friendCapsuleList(memberId)
            if(response.isSuccessful){
                val jsonString = response.body()?.string()
                val jsonObject = JSONObject(jsonString)
                val dataObjects = jsonObject.getJSONObject("data")

                val unopenedCapsuleDto = dataObjects.getJSONArray("unopenedCapsuleDtoList")
                unopenedCapsuleDtoList = mutableListOf()
                for(i in 0 until unopenedCapsuleDto.length()){
                    val curr = unopenedCapsuleDto.getJSONObject(i)
                    val capsuleId = curr.getInt("capsuleId")
                    val openDate = curr.getString("openDate")
                    val address = curr.getString("address")
                    val isLocked = curr.getBoolean("isLocked")
                    unopenedCapsuleDtoList.add(UnopenedCapsuleDto(capsuleId,openDate,address,isLocked))
                }

                val openedCapsuleDto = dataObjects.getJSONArray("openedCapsuleDtoList")
                openedCapsuleDtoList = mutableListOf()
                for(i in 0 until openedCapsuleDto.length()){
                    val curr = openedCapsuleDto.getJSONObject(i)
                    val capsuleId = curr.getInt("capsuleId")
                    val openDate = curr.getString("openDate")
                    val address = curr.getString("address")
                    val isAdded = curr.getBoolean("isAdded")
                    openedCapsuleDtoList.add(OpenedCapsuleDto(capsuleId,openDate,address,isAdded))
                }

                val mapInfoDto = dataObjects.getJSONArray("mapInfoDtoList")
                mapInfoDtoList = mutableListOf()
                for(i in 0 until mapInfoDto.length()){
                    val curr = mapInfoDto.getJSONObject(i)
                    val capsuleId = curr.getInt("capsuleId")
                    val latitude =curr.getDouble("latitude")
                    val longitude = curr.getDouble("longitude")
                    val isOpened = curr.getBoolean("isOpened")
                    val isLocked = curr.getBoolean("isLocked")
                    mapInfoDtoList.add(MapInfoDto(capsuleId,latitude,longitude,isOpened,isLocked))
                }

                val getCapsuleListRes = GetCapsuleListRes(unopenedCapsuleDtoList, openedCapsuleDtoList, mapInfoDtoList)

                friendCapsuleList.value = getCapsuleListRes

                Log.d(TAG,"getFriendCapsuleList : 응답 성공 / $dataObjects")
            }else {
                Log.d(TAG,"getFriendCapsuleList : 응답 실패 / ${response.message()}" )
            }
        }
    }

    // 나의 방문 기록
    fun getVisitedCapsuleList(memberId : Int){
        viewModelScope.launch {
            var response = repository.visitedCapsuleList(memberId)
            if(response.isSuccessful){
                val jsonString = response.body()?.string()
                val jsonObject = JSONObject(jsonString)
                val dataObjects = jsonObject.getJSONObject("data")

                val openedCapsuleDto = dataObjects.getJSONArray("openedCapsuleDtoList")
                openedCapsuleDtoList = mutableListOf()
                for(i in 0 until openedCapsuleDto.length()){
                    val curr = openedCapsuleDto.getJSONObject(i)
                    val capsuleId = curr.getInt("capsuleId")
                    val openDate = curr.getString("openDate")
                    val address = curr.getString("address")
                    val isAdded = curr.getBoolean("isAdded")
                    openedCapsuleDtoList.add(OpenedCapsuleDto(capsuleId,openDate,address,isAdded))
                }

                val mapInfoDto = dataObjects.getJSONArray("mapInfoDtoList")
                mapInfoDtoList = mutableListOf()
                for(i in 0 until mapInfoDto.length()){
                    val curr = mapInfoDto.getJSONObject(i)
                    val capsuleId = curr.getInt("capsuleId")
                    val latitude =curr.getDouble("latitude")
                    val longitude = curr.getDouble("longitude")
                    val isOpened = curr.getBoolean("isOpened")
                    val isLocked = curr.getBoolean("isLocked")
                    mapInfoDtoList.add(MapInfoDto(capsuleId,latitude,longitude,isOpened,isLocked))
                }

                val getVisitedListRes = GetVisitedListRes( openedCapsuleDtoList, mapInfoDtoList )

                visitedCapsuleList.value = getVisitedListRes

                Log.d(TAG,"getVisitedCapsuleList : 응답 성공 / $dataObjects")
            }else{
                Log.d(TAG,"getVisitedCapsuleList : 응답 실패 / ${response.message()}" )
            }
        }
    }

    // 캡슐 삭제
    fun removeCapsule(capsuleId : Int){
        viewModelScope.launch {
            var response = repository.capsuleRemove(capsuleId)
            if(response.isSuccessful){
                Log.d(TAG, "removeCapsule : 응답 성공 / ${response.body()?.string()} ")
            }else{
                Log.d(TAG, "removeCapsule : 응답 실패 / ${response.message()} ")
            }
        }
    }

    // 캡슐 공개범위 수정
    fun modifyCapsule(capsuleId : Int , rangeType : String){
        viewModelScope.launch {
            var response = repository.capsuleModify(capsuleId,rangeType)
            if(response.isSuccessful){
                Log.d(TAG, "modifyCapsule : 응답 성공 / ${response.body()?.string()} ")
            }else{
                Log.d(TAG, "modifyCapsule : 응답 실패 / ${response.message()} ")
            }

        }
    }

    // 그룹 멤버 조회
    fun getGroupMemberList(capsuleId : Int){
        viewModelScope.launch {
            var response = repository.groupMemberList(capsuleId)
            if(response.isSuccessful){
                val jsonString = response.body()?.string()
                val jsonObject = JSONObject(jsonString)
                val groupMemberDto = jsonObject.getJSONArray("data")
                groupMemberDtoList = mutableListOf()
                for(i in 0 until groupMemberDto.length()){
                    val curr = groupMemberDto.getJSONObject(i)
                    val memberId = curr.getInt("memberId")
                    val nickname = curr.getString("nickname")
                    val profileImageUrl = curr.getString("profileImageUrl")
                    groupMemberDtoList.add(GroupMemberDto(memberId,nickname,profileImageUrl))
                }

                val getGroupMemberRes = GetGroupMemberRes(groupMemberDtoList)

                groupMemberList.value = getGroupMemberRes

                Log.d(TAG, "getGroupMemberList : 응답 성공 / $jsonObject ")
            }
            Log.d(TAG, "getGroupMemberList : 응답 실패/ ${response.message()}")
        }
    }

    // 1km 이내 캡슐 조회
    fun getAroundCapsuleList(capsuleId: Int, latitude : Double, longitude : Double){
            viewModelScope.launch {
                var response = repository.aroundCapsule(capsuleId,latitude,longitude)
                if(response.isSuccessful){
                    val jsonString = response.body()?.string()
                    val jsonObject = JSONObject(jsonString)
                    val aroundCapsuleDto = jsonObject.getJSONArray("data")
                    aroundCapsuleDtoList = mutableListOf()
                    for(i in 0 until aroundCapsuleDto.length()){
                        val curr = aroundCapsuleDto.getJSONObject(i)
                        val capsuleId = curr.getInt("capsuleId")
                        val memberNickname = curr.getString("memberNickname")
                        val address = curr.getString("address")
                        aroundCapsuleDtoList.add(AroundCapsuleDto(capsuleId,memberNickname,address))
                    }

                    val getAroundCapsuleListRes = GetAroundCapsuleListRes(aroundCapsuleDtoList)

                    aroundCapsuleList.value = getAroundCapsuleListRes

                    Log.d(TAG, "getAroundCapsuleList : 응답 성공 / $jsonObject ")
                }
                Log.d(TAG, "getAroundCapsuleList : 응답 실패/ ${response.message()}")

            }
    }

    // 친구 목록 조회
    fun getMyFriendList(memberId :Int){
        viewModelScope.launch {
            var response =repository.myFriendList(memberId)
            if(response.isSuccessful){
                val jsonString = response.body()?.string()
                val jsonObject = JSONObject(jsonString)
                val friendDto = jsonObject.getJSONArray("data")
                myfriendDtoList = mutableListOf()
                for(i in 0 until friendDto.length()){
                    val curr = friendDto.getJSONObject(i)
                    val memberId = curr.getInt("memberId")
                    val nickname = curr.getString("nickname")
                    val profileImageUrl = curr.getString("profileImageUrl")
                    myfriendDtoList.add(FriendDto(memberId,nickname, profileImageUrl))
                }

                 val getMyFriendListRes = GetFriendListRes(myfriendDtoList)

                friendList.value = getMyFriendListRes

                Log.d(TAG, "getMyFriendList : 응답 성공 / $jsonObject ")
            }else{
                Log.d(TAG, "getMyFriendList : 응답 실패/ ${response.message()}")
            }
        }
    }

}