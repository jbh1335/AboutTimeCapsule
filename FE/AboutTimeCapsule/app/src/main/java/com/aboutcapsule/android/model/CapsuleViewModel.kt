package com.aboutcapsule.android.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aboutcapsule.android.data.capsule.GetCapsuleListRes
import com.aboutcapsule.android.data.capsule.GetVisitedListRes
import com.aboutcapsule.android.data.capsule.MapInfoDto
import com.aboutcapsule.android.data.capsule.OpenedCapsuleDto
import com.aboutcapsule.android.data.capsule.UnopenedCapsuleDto
import com.aboutcapsule.android.repository.CapsuleRepo
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import java.lang.Exception

class CapsuleViewModel(private val repository : CapsuleRepo) : ViewModel() {
    private var TAG = "CapsuleViewModel"
    var myCapsuleList : MutableLiveData<GetCapsuleListRes> = MutableLiveData()
    var friendCapsuleList : MutableLiveData<GetCapsuleListRes> = MutableLiveData()
    var visitedCapsuleList : MutableLiveData<GetVisitedListRes> = MutableLiveData()

    companion object{
        lateinit var unopenedCapsuleDtoList : MutableList<UnopenedCapsuleDto>
        lateinit var openedCapsuleDtoList : MutableList<OpenedCapsuleDto>
        lateinit var mapInfoDtoList : MutableList<MapInfoDto>
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

}