package com.aboutcapsule.android.repository


import android.provider.ContactsContract.CommonDataKinds.Nickname
import android.util.Log
import com.aboutcapsule.android.util.RetrofitManager
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Path

class MypageRepo {
    suspend fun getMyPage(memberId: Int?, otherMemberId: Int): Response<ResponseBody> {
        return RetrofitManager.memberInstacne.api.getMyPage(memberId, otherMemberId)
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

    suspend fun checkNickname(nickname: String) : Response<ResponseBody> {
        return RetrofitManager.memberInstacne.api.checkNickname(nickname)
    }
    suspend fun modifyNickname(memberId: Int, nickname: String) : Response<ResponseBody> {
        return RetrofitManager.memberInstacne.api.modifyNickname(memberId, nickname)
    }

    suspend fun sendFriendRequest(fromMemberId: Int, toMemberId: Int) : Response<ResponseBody> {
        return RetrofitManager.memberInstacne.api.sendFriendRequest(fromMemberId, toMemberId)
    }

    suspend fun deleteFriend(friendId: Int) : Response<ResponseBody> {
        return RetrofitManager.memberInstacne.api.deleteFriend(friendId)
    }

    suspend fun findFriend(memberId: Int, nickname: String): Response<ResponseBody> {
        return RetrofitManager.memberInstacne.api.findFriend(memberId, nickname)
    }


}