package com.aboutcapsule.android.repository.mypage


import android.util.Log
import com.aboutcapsule.android.data.mypage.GetMyPageRes
import com.aboutcapsule.android.util.RetrofitManager
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit

class MypageRepo {
    suspend fun getMyPage(memberId: Int): Response<ResponseBody> {
//        Log.i("마이페이지리스폰스", "${RetrofitManager.memberInstacne.api.getMyPage(memberId).body()?.string()}")
        return RetrofitManager.memberInstacne.api.getMyPage(memberId)

    }
    suspend fun acceptFriendRequest(friendId:Int?) : Response<ResponseBody> {

        return RetrofitManager.memberInstacne.api.acceptFriendRequest(friendId)
    }
    suspend fun refuseFriendRequest(friendId: Int?) : Response<ResponseBody> {
        return RetrofitManager.memberInstacne.api.refuseFriendRequest(friendId)
    }

}