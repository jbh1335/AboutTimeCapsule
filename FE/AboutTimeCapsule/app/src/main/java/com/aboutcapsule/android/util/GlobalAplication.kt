package com.aboutcapsule.android.util

import android.app.Application
import androidx.core.os.bundleOf
import com.aboutcapsule.android.BuildConfig
import com.kakao.sdk.common.KakaoSdk
import com.navercorp.nid.NaverIdLoginSDK
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GlobalAplication:Application() {

    companion object {
        lateinit var preferences: PreferenceUtil

        private lateinit var globalAplication: GlobalAplication
        fun getInstance() : GlobalAplication = globalAplication
    }
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, BuildConfig.KAKAO_APP_KEY)
        NaverIdLoginSDK.initialize(this, BuildConfig.NAVER_CLIENT_ID, BuildConfig.NAVER_CLIENT_SECRET, "어바웃타임캡슐")
        preferences = PreferenceUtil(applicationContext)
        globalAplication = this
        preferences.remove("friendId")
//        preferences.clear()ㅁ
        preferences.setInt("currentUser", 5)
        preferences.setString("currentUserNickname", "정훈")
//        preferences.setInt("currentUserNickname", )




    }






}