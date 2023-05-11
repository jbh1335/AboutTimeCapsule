package com.aboutcapsule.android.util

import android.util.Log
import com.aboutcapsule.android.service.CapsuleService
import com.aboutcapsule.android.service.MemberService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Member

class RetrofitManager {
    companion object {
        private const val BASE_URL = "https://k8b302.p.ssafy.io:"
        private const val memberPort = "9001/api/member/"
        private const val capsulePort = "9003/api/capsule/"
        private const val chatPort = "9004"
    }
        val getMemberRetrofit = Retrofit.Builder()
                .baseUrl(BASE_URL+ memberPort)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    object memberInstacne {
        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL+ memberPort)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        val api: MemberService by lazy {
            retrofit.create(MemberService::class.java)
        }
    }
    object capsuleInstance {
        private val retrofit by lazy{
            Retrofit.Builder()
                .baseUrl(BASE_URL + capsulePort)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        val api: CapsuleService by lazy{
            retrofit.create(CapsuleService::class.java)
        }
    }

}
