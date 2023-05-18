package com.aboutcapsule.android.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aboutcapsule.android.data.GetAlramListRes
import com.aboutcapsule.android.data.RegistAlramToeknReq
import com.aboutcapsule.android.repository.AlramRepo
import kotlinx.coroutines.launch
import org.json.JSONObject

class AlramViewModel(private val repository : AlramRepo) :ViewModel() {
    companion object {
        var alramList = MutableLiveData<MutableList<GetAlramListRes>>()
    }

    fun registAlramToken(registAlramToeknReq: RegistAlramToeknReq) {
        viewModelScope.launch {
            val response = repository.registAlramToken(registAlramToeknReq)
            if (response.isSuccessful) {
                Log.d("알람 토큰 생성 완료", "${response.code()} / ${response.message()}")
            } else {
                Log.d("알람 토큰 생성 실패", "${response.code()} / ${response.message()}")
            }
        }
    }

    fun getAlramList(memberId: Int) {
        viewModelScope.launch {
            val response = repository.getAlramList(memberId)
            if (response.isSuccessful) {
                Log.d("알람 내역 조회", "${response.code()}")
                val jsonString = response.body()?.string()
                val jsonObject = JSONObject(jsonString)
                val dataArrays = jsonObject.getJSONArray("data")
                var mutableList = mutableListOf<GetAlramListRes>()
                for (i in 0 until mutableList.size) {
                    var dataList = dataArrays.getJSONObject(i)
                    val alramId = dataList.getInt("alarmId")
                    val memberId = dataList.getInt("memberId")
                    val capsuleId = dataList.getInt("capsuleId")
                    val content = dataList.getString("content")
                    val categoryType = dataList.getString("categoryType")
                    val getAlramListRes = GetAlramListRes(alramId, memberId, capsuleId, content,categoryType,)
                    mutableList.add(getAlramListRes)
                }
                alramList.value = mutableList
            } else {
                Log.d("알람 내역 조회 실패", "${response.code()}")
            }
        }
    }

}