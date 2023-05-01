package com.aboutcapsule.android.views.login

import android.app.Application
import com.aboutcapsule.android.BuildConfig
import com.kakao.sdk.common.KakaoSdk

class GlobalAplication:Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, BuildConfig.NATIVE_APP_KEY)

    }


}