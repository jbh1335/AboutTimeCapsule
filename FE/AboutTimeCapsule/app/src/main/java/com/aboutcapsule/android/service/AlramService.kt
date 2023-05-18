package com.aboutcapsule.android.service

import com.aboutcapsule.android.data.RegistAlramToeknReq
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface AlramService {

    @GET("list/{memberId}")
    suspend fun getAlramList(@Path("memberId") memberId: Int) : Response<ResponseBody>

    @PATCH("token/regist")
    suspend fun registAlramToken(registAlramToeknReq: RegistAlramToeknReq) : Response<ResponseBody>

}