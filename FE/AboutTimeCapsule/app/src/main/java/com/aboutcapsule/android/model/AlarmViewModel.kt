package com.aboutcapsule.android.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aboutcapsule.android.repository.AlarmRepo
import kotlinx.coroutines.launch

class AlarmViewModel(private val repository: AlarmRepo) : ViewModel() {
    private var TAG = "AlarmViewModel"


    fun getAlarmList(memberId : Int ){
       viewModelScope.launch {
           val response = repository.alarmList(memberId)
           if(response.isSuccessful){

               Log.d(TAG,"getAlarmList / ${response.body()}")
           }else{
               Log.d(TAG,"getAlarmList / ${response.message()}")
           }



       }
    }
}