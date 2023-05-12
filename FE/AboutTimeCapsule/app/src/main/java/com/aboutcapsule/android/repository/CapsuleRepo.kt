package com.aboutcapsule.android.repository

import com.aboutcapsule.android.util.RetrofitManager
import okhttp3.ResponseBody
import retrofit2.Response

class CapsuleRepo {

    suspend fun myCapsuleList(memberId : Int) : Response<ResponseBody> {
        return RetrofitManager.capsuleInstance.api.findMyCapsule(memberId)
    }

    suspend fun friendCapsuleList(memberId : Int ) : Response<ResponseBody> {
        return RetrofitManager.capsuleInstance.api.findFriendCapsule(memberId)
    }
}