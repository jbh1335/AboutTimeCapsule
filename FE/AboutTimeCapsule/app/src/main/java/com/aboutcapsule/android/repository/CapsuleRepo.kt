package com.aboutcapsule.android.repository

import com.aboutcapsule.android.data.capsule.AroundCapsuleReq
import com.aboutcapsule.android.data.capsule.AroundPopularPlaceReq
import com.aboutcapsule.android.data.capsule.CapsuleDetailReq
import com.aboutcapsule.android.data.capsule.MapAroundCapsuleReq
import com.aboutcapsule.android.data.capsule.MapCapsuleDetailReq
import com.aboutcapsule.android.data.capsule.PostRegistCapsuleReq
import com.aboutcapsule.android.util.RetrofitManager
import okhttp3.ResponseBody
import retrofit2.Response

class CapsuleRepo {

    suspend fun capsuleAdd(postRegistCapsuleReq: PostRegistCapsuleReq) : Response<ResponseBody>{
        return RetrofitManager.capsuleInstance.api.addCapsule(postRegistCapsuleReq)
    }

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

    suspend fun aroundCapsule(aroundCapsuleReq : AroundCapsuleReq) : Response<ResponseBody>{
        return RetrofitManager.capsuleInstance.api.findAroundCapsule(aroundCapsuleReq)
    }

    suspend fun myFriendList(memberId : Int) : Response<ResponseBody>{
        return RetrofitManager.capsuleInstance.api.findFriendList(memberId)
    }

    suspend fun capsuleCount(memberId: Int) : Response<ResponseBody>{
        return RetrofitManager.capsuleInstance.api.findCapsuleCount(memberId)
    }

    suspend fun aroundCapsuleInMap(mapAroundCapsuleReq: MapAroundCapsuleReq) : Response<ResponseBody>{
        return RetrofitManager.capsuleInstance.api.findAroundCapsuleInMap(mapAroundCapsuleReq)
    }

    suspend fun capsuleInMapDetail(mapCapsuleDetailReq : MapCapsuleDetailReq) : Response<ResponseBody>{
        return RetrofitManager.capsuleInstance.api.findCapsuleInMapDetail(mapCapsuleDetailReq)
    }

    suspend fun capsuleDetail(capsuleDetailReq: CapsuleDetailReq) : Response<ResponseBody>{
        return RetrofitManager.capsuleInstance.api.findCapsuleDetail(capsuleDetailReq)
    }

    suspend fun aroundPopularPlaceList(aroundPopularPlaceReq: AroundPopularPlaceReq): Response<ResponseBody>{
        return RetrofitManager.capsuleInstance.api.findAroundPopularPlace((aroundPopularPlaceReq))
    }

}