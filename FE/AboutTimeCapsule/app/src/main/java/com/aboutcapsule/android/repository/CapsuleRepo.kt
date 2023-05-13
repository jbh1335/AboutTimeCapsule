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

    suspend fun visitedCapsuleList(memberId : Int ) : Response<ResponseBody> {
        return RetrofitManager.capsuleInstance.api.findVisited(memberId)
    }

    suspend fun capsuleRemove(capsuleId : Int ) : Response<ResponseBody> {
        return RetrofitManager.capsuleInstance.api.removeCapsule(capsuleId)
    }

    suspend fun capsuleModify(capsuleId : Int , rangeType : String) : Response<ResponseBody> {
        return RetrofitManager.capsuleInstance.api.modifyCapsule(capsuleId,rangeType)
    }

    suspend fun groupMemberList(capsuleId : Int) : Response<ResponseBody>{
        return RetrofitManager.capsuleInstance.api.findGroupMember(capsuleId)
    }
}