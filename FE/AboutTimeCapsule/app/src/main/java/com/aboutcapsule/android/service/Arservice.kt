package com.aboutcapsule.android.service

import com.aboutcapsule.android.data.GetCapsuleNearReq
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET

interface Arservice {
    @GET("map")
    suspend fun getCapsuleNear (@Body getCapsuleNearReq: GetCapsuleNearReq) : Response<ResponseBody>
}