package com.aboutcapsule.android.util

import com.aboutcapsule.android.service.Arservice
import com.aboutcapsule.android.service.CapsuleService
import com.aboutcapsule.android.service.MemberService
import com.aboutcapsule.android.service.MemoryService
import com.aboutcapsule.android.service.OauthService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitManager {
    companion object {

        private const val BASE_URL = "https://k8b302.p.ssafy.io/"
        private const val memberPort = "api/member/"
        private const val capsulePort = "api/capsule/"
        private const val oauthPort = "api/oauth/"
        private const val alarmPort = "api/alarm/"

    }


    object oauthInstance {
        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL+ oauthPort)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        val api: OauthService by lazy {
            retrofit.create(OauthService::class.java)
        }
    }
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
    object memoryInstance {
        private val retrofit by lazy{
            Retrofit.Builder()
                .baseUrl(BASE_URL + capsulePort)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        val api: MemoryService by lazy{
            retrofit.create(MemoryService::class.java)
        }
        val okHttpClient by lazy {
            OkHttpClient.Builder()
                .build()
        }
    }
    object ArInstance {
        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL + capsulePort)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        val api: Arservice by lazy {
            retrofit.create(Arservice::class.java)
        }
    }

}
