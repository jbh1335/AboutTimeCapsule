package com.aboutcapsule.android.service

import com.aboutcapsule.android.data.capsule.MapAroundCapsuleReq
import com.aboutcapsule.android.data.capsule.PostRegistCapsuleReq
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CapsuleService {

    @POST("regist")
    suspend fun addCapsule (@Body postRegistCapsuleReq : PostRegistCapsuleReq ) : Response<ResponseBody>

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

    @GET("group/{capsuleId}")
    suspend fun findGroupMember(@Path("capsuleId") capsuleId:Int) : Response<ResponseBody>

    @GET("around")
    suspend fun findAroundCapsule(@Query("memberId") memberId: Int, @Query("latitude") latitude : Double,@Query("longitude") longitude : Double ) : Response<ResponseBody>

    @GET("friend/{memberId}")
    suspend fun findFriendList(@Path("memberId") memberId : Int ): Response<ResponseBody>

    @GET("main/{memberId}")
    suspend fun findCapsuleCount(@Path("memberId") memberId: Int ): Response<ResponseBody>

    @POST("map")
    suspend fun findAroundCapsuleInMap(@Body mapAroundCapsuleReq: MapAroundCapsuleReq ) : Response<ResponseBody>

}