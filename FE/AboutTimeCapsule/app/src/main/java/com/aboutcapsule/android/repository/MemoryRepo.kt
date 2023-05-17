package com.aboutcapsule.android.repository

import android.util.Log
import com.aboutcapsule.android.data.memory.GroupOpenDateReq
import com.aboutcapsule.android.data.memory.MemoryRegistReq
import com.aboutcapsule.android.data.memory.MemoryReq
import com.aboutcapsule.android.data.memory.PostCommentReq
import com.aboutcapsule.android.util.RetrofitManager
import com.aboutcapsule.android.views.capsule.ArticleRegistFragment
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Path

class MemoryRepo {
    suspend fun registerMemory(imageList: ArrayList<MultipartBody.Part>, memoryRegistReq: RequestBody): Response<ResponseBody> {
        Log.d("레포", "${ArticleRegistFragment.imageList}")
        Log.d("레포req", "${memoryRegistReq}")
        return RetrofitManager.memoryInstance.api.registerMemory(imageList, memoryRegistReq)
    }
    suspend fun getCapsuleMemory(memoryReq: MemoryReq) : Response<ResponseBody>{
        return RetrofitManager.memoryInstance.api.getCapsuleMemory(memoryReq)
    }
    suspend fun getMemoryComments(memoryId: Int) : Response<ResponseBody> {
        return RetrofitManager.memoryInstance.api.getMemoryComments(memoryId)
    }
    suspend fun postMemoryComment(postCommentReq: PostCommentReq) : Response<ResponseBody> {
        return RetrofitManager.memoryInstance.api.postMemoryComment(postCommentReq)
    }
    suspend fun sealMemoryFirst(groupOpenDateReq: GroupOpenDateReq) : Response<ResponseBody> {
        return RetrofitManager.memoryInstance.api.sealMemoryFirst(groupOpenDateReq)
    }
}