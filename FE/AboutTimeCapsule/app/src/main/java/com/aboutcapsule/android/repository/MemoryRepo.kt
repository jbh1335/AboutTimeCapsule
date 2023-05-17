package com.aboutcapsule.android.repository

import android.util.Log
import com.aboutcapsule.android.data.memory.MemoryRegistReq
import com.aboutcapsule.android.util.RetrofitManager
import com.aboutcapsule.android.views.capsule.ArticleRegistFragment
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response

class MemoryRepo {
    suspend fun registerMemory(imageList: ArrayList<MultipartBody.Part>, memoryRegistReq: RequestBody): Response<ResponseBody> {
        Log.d("레포", "${ArticleRegistFragment.imageList}")
        Log.d("레포req", "${memoryRegistReq}")
        return RetrofitManager.memoryInstance.api.registerMemory(imageList, memoryRegistReq)
    }
}