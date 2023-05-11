package com.aboutcapsule.android.service

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CapsuleService {

    @GET("me/list/{memberId}")
    suspend fun findMyCapsule (@Path("memberId") memberId : Int) : Response<ResponseBody>

    @GET("friend/list/{memberId}")
    suspend fun findFriendCapsule (@Path("memberId") memberId : Int ) : Response<ResponseBody>

}