package com.aboutcapsule.android.service

import com.aboutcapsule.android.data.memory.GroupOpenDateReq
import com.aboutcapsule.android.data.memory.MemoryRegistReq
import com.aboutcapsule.android.data.memory.MemoryReq
import com.aboutcapsule.android.data.memory.PostCommentReq
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.jetbrains.annotations.Nullable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface MemoryService {
    @Multipart
    @POST("memory/regist")
    suspend fun registerMemory(@Part multipartFileList: List<MultipartBody.Part>,
                               @Part("memoryRegistReq") memoryRegistReq : RequestBody): Response<ResponseBody>

    @POST("memory")
    suspend fun getCapsuleMemory(@Body memoryReq: MemoryReq) :Response<ResponseBody>

    @POST("memory/comment/regist")
    suspend fun postMemoryComment(@Body postCommentReq: PostCommentReq) : Response<ResponseBody>
    @GET("memory/comment/{memoryId}")
    suspend fun getMemoryComments(@Path("memoryId") memoryId: Int) : Response<ResponseBody>

    @PATCH("memory/openDate")
    suspend fun sealMemoryFirst(@Body groupOpenDateReq: GroupOpenDateReq) : Response<ResponseBody>
}