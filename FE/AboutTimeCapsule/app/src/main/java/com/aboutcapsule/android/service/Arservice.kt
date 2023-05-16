package com.aboutcapsule.android.service

import com.aboutcapsule.android.data.capsule.PostRegistCapsuleReq
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface Arservice {
    @POST("map")
    suspend fun getCapsuleNear (@Body postRegistCapsuleReq : PostRegistCapsuleReq) : Response<ResponseBody>
}