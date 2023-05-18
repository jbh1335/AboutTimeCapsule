package com.aboutcapsule.android.service

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface AlarmSerivce {

    @GET("list/{memberId}")
    suspend fun findAlarmList(@Path("memberId") memberId : Int ) : Response<ResponseBody>
}