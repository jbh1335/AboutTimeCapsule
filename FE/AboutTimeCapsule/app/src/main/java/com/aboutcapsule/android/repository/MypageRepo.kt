package com.aboutcapsule.android.repository


import android.provider.ContactsContract.CommonDataKinds.Nickname
import com.aboutcapsule.android.util.RetrofitManager
import okhttp3.ResponseBody
import retrofit2.Response

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

    suspend fun checkNickname(nickname: String) : Response<ResponseBody> {
        return RetrofitManager.memberInstacne.api.checkNickname(nickname)
    }

}