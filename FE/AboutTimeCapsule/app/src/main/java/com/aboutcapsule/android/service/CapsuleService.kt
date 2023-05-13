package com.aboutcapsule.android.service

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface CapsuleService {

    @GET("me/list/{memberId}")
    suspend fun findMyCapsule (@Path("memberId") memberId : Int) : Response<ResponseBody>

    @GET("friend/list/{memberId}")
    suspend fun findFriendCapsule (@Path("memberId") memberId : Int ) : Response<ResponseBody>

    @GET("open/list/{memberId}")
    suspend fun findVisited (@Path("memberId") memberId : Int ) : Response<ResponseBody>

    @PATCH("delete/{capsuleId}")
    suspend fun removeCapsule (@Path("capsuleId") capsuleId : Int ): Response<ResponseBody>

    @PATCH("modify/{capsuleId}/{rangeType}")
    suspend fun modifyCapsule (@Path("capsuleId") capsuleId: Int , @Path("rangeType") rangeType: String) : Response<ResponseBody>

}