package com.aboutcapsule.android.repository


import com.aboutcapsule.android.util.RetrofitManager
import okhttp3.ResponseBody
import retrofit2.Response

class MypageRepo {
    suspend fun getMyPage(memberId: Int?): Response<ResponseBody> {
        return RetrofitManager.memberInstacne.api.getMyPage(memberId)

    }
    suspend fun acceptFriendRequest(friendId:Int?) : Response<ResponseBody> {

        return RetrofitManager.memberInstacne.api.acceptFriendRequest(friendId)
    }
    suspend fun refuseFriendRequest(friendId: Int?) : Response<ResponseBody> {
        return RetrofitManager.memberInstacne.api.refuseFriendRequest(friendId)
    }
    suspend fun getMyAllFriend(memberId: Int?) : Response<ResponseBody> {
        return RetrofitManager.memberInstacne.api.getMyAllFriend(memberId)
    }

}