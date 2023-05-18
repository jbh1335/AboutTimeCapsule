package com.aboutcapsule.android.repository

import com.aboutcapsule.android.data.RegistAlramToeknReq
import com.aboutcapsule.android.util.RetrofitManager
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Path

class AlramRepo {
    suspend fun getAlramList(memberId: Int) : Response<ResponseBody> {
        return RetrofitManager.AlramInstance.api.getAlramList(memberId)
    }

    suspend fun registAlramToken(registAlramToeknReq: RegistAlramToeknReq) : Response<ResponseBody> {
        return RetrofitManager.AlramInstance.api.registAlramToken(registAlramToeknReq)
    }
}