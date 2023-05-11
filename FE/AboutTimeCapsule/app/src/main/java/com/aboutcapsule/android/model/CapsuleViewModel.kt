package com.aboutcapsule.android.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aboutcapsule.android.data.capsule.GetCapsuleListRes
import com.aboutcapsule.android.repository.CapsuleRepo
import kotlinx.coroutines.launch

class CapsuleViewModel(private val repository : CapsuleRepo) : ViewModel() {
    private var TAG = "CapsuleViewModel"
    var myCapsuleList : MutableLiveData<GetCapsuleListRes> = MutableLiveData()
    var friendCapsuleList : MutableLiveData<GetCapsuleListRes> = MutableLiveData()

    // 나의 캡슐 조회
    fun getMyCapsuleList(memberId : Int) {
        viewModelScope.launch {
            val response = repository.myCapsuleList(memberId)
            if(response.isSuccessful){
                Log.d(TAG,"${response.body()?.string()}")
            }else {
                Log.d(TAG,"getMyCapsuleList : 응답 실패")
            }
        }
    }

    // 친구 캡슐 조회
    fun getFriendCapsuleList(memberId : Int ){
        viewModelScope.launch {
            val response = repository.friendCapsuleList(memberId)
            if(response.isSuccessful){
                Log.d(TAG,"${response.body()?.string()}")
            }else {
                Log.d(TAG,"getFriendCapsuleList : 응답 실패 ")
            }

        }
    }



}