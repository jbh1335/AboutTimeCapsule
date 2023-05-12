package com.aboutcapsule.android.service

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface OauthService {

    @POST("login/{provider}")
    suspend fun doLogin (@Path("provider") provider:String, @Header("Authorization") accessToken:String) : Response<ResponseBody>


}