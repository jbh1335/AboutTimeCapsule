package com.aboutcapsule.android.service

import retrofit2.http.PATCH

interface AlarmService {

    @PATCH
    suspend fun storeAlramToekn()
}