package com.aboutcapsule.android.service

import com.aboutcapsule.android.data.memory.MemoryRegistReq
import com.aboutcapsule.android.data.memory.MemoryReq
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.jetbrains.annotations.Nullable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface MemoryService {
    @Multipart
    @POST("regist")
    suspend fun registerMemory(@Part multipartFileList: List<MultipartBody.Part>,
                               @Part("memoryRegistReq") memoryRegistReq : RequestBody): Response<ResponseBody>

    @POST("/")
    suspend fun getCapsuleMemory(@Body memoryReq: MemoryReq) :Response<ResponseBody>
}