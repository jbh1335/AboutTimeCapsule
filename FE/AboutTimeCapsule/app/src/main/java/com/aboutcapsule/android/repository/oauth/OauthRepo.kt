package com.aboutcapsule.android.repository.oauth

import com.aboutcapsule.android.util.RetrofitManager
import okhttp3.ResponseBody
import retrofit2.Response

class OauthRepo {
    suspend fun doLogin(provider: String, accessToken: String): Response<ResponseBody> {

        return RetrofitManager.oauthInstance.api.doLogin(provider, accessToken)
    }
}