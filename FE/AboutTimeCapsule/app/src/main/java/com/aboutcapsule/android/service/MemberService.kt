package com.aboutcapsule.android.service


import com.aboutcapsule.android.data.mypage.GetMyPageRes

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface MemberService {
    @GET("mypage/{memberId}")
    suspend fun getMyPage (@Path("memberId") memberId:Int) : Response<ResponseBody>

}