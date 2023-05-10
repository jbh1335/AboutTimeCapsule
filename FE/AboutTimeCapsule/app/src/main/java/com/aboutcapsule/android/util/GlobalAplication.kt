package com.aboutcapsule.android.util

import android.app.Application
import com.aboutcapsule.android.BuildConfig
import com.kakao.sdk.common.KakaoSdk
import com.navercorp.nid.NaverIdLoginSDK
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GlobalAplication:Application() {
    private lateinit var dataStore: DataStoreModule

    companion object {
        private lateinit var globalAplication: GlobalAplication
        fun getInstance() : GlobalAplication = globalAplication
    }
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, BuildConfig.KAKAO_APP_KEY)
        NaverIdLoginSDK.initialize(this, BuildConfig.NAVER_CLIENT_ID, BuildConfig.NAVER_CLIENT_SECRET, "어바웃타임캡슐")

        globalAplication = this
        dataStore = DataStoreModule(this)
    }
    fun getDataStore() : DataStoreModule = dataStore





}