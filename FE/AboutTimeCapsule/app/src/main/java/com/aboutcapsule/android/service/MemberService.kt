package com.aboutcapsule.android.service


import com.aboutcapsule.android.data.mypage.GetMyPageRes

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface MemberService {
    @GET("mypage/{memberId}/{otherMemberId}")
    suspend fun getMyPage (@Path("memberId") memberId:Int?, @Path("otherMemberId") otherMemberId: Int) : Response<ResponseBody>

    @PATCH("request/accept/{friendId}")
    suspend fun acceptFriendRequest(@Path("friendId") friendId:Int?) : Response<ResponseBody>

    @DELETE("request/refuse/{friendId}")
    suspend fun refuseFriendRequest(@Path("friendId") friendId: Int?) : Response<ResponseBody>

    @GET("nickname/{nickname}/exists")
    suspend fun checkNickname(@Path("nickname") nickname: String) : Response<ResponseBody>

    @PUT("nickname/{memberId}/{nickname}")
    suspend fun modifyNickname(@Path("memberId") memberId : Int, @Path("nickname") nickname: String) : Response<ResponseBody>


    @GET("friend/{memberId}")
    suspend fun getMyAllFriend(@Path("memberId") memberId: Int?) : Response<ResponseBody>

    @POST("request/{fromMemberId}/{toMemberId}")
    suspend fun sendFriendRequest
                (@Path("fromMemberId") fromMemberId: Int, @Path("toMemberId") toMemberId: Int)
    : Response<ResponseBody>

    @DELETE("friend/delete/{friendId}")
    suspend fun deleteFriend(@Path("friendId") friendId: Int) : Response<ResponseBody>

    @GET("search/{memberId}/{nickname}")
    suspend fun findFriend(@Path("memberId") memberId: Int, @Path("nickname") nickname: String)
    : Response<ResponseBody>




}