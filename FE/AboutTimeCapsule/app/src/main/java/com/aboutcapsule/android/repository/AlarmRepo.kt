package com.aboutcapsule.android.repository

import com.aboutcapsule.android.util.RetrofitManager
import okhttp3.ResponseBody
import retrofit2.Response

class AlarmRepo {

    suspend fun alarmList(memberId : Int) :Response<ResponseBody>{
        return RetrofitManager.alarmInstance.api.findAlarmList(memberId)
    }
}