package com.aboutcapsule.android.repository

import com.aboutcapsule.android.data.capsule.PostRegistCapsuleReq
import com.aboutcapsule.android.util.RetrofitManager
import okhttp3.ResponseBody
import retrofit2.Response

class ArRepo {

    suspend fun getCapsuleNear(postRegistCapsuleReq: PostRegistCapsuleReq) : Response<ResponseBody> {
        return RetrofitManager.ArInstance.api.getCapsuleNear(postRegistCapsuleReq)
    }
}