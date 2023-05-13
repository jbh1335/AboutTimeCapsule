package com.aboutcapsule.android.util

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class DataStoreModule(private val context : Context) {
    private val Context.dataStore  by preferencesDataStore(name = "dataStore")

    private val currentMemberId = intPreferencesKey("currentMemberId")

    // 현재 유저의 키 값과 대응되는 값 반환
    val getcurrentMemberId : Flow<Int> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map {preferences ->
            preferences[currentMemberId] ?: 0
        }

    // 현재 유저정보 불러오기
    suspend fun setCurrentUser(userId : Int){
        context.dataStore.edit { preferences ->
            preferences[currentMemberId] = userId
        }
    }



}