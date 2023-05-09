package com.aboutcapsule.android.service

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface OauthService {

    @GET("callback/{provider}")
    suspend fun doLogin (@Path("provider") provider:String, @Header("Authorization") accessToken:String) : Response<ResponseBody>
}