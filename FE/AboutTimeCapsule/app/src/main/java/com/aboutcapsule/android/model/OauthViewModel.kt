package com.aboutcapsule.android.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aboutcapsule.android.data.oauth.LoginRes
import com.aboutcapsule.android.repository.OauthRepo
import kotlinx.coroutines.launch
import org.json.JSONObject

class OauthViewModel(private val repository: OauthRepo) : ViewModel() {
    var loginInstance : MutableLiveData<LoginRes> = MutableLiveData()

    fun doLogin(provider:String, accessToken:String) {
        viewModelScope.launch {
            val response = repository.doLogin(provider, accessToken)
            if(response.isSuccessful) {
                val jsonString = response.body()?.string()
                val jsonObject = JSONObject(jsonString)
                val dataObjects = jsonObject.getJSONObject("data")

                val id = dataObjects.getInt("id")
                val nickname = dataObjects.getString("nickname")
                val email = dataObjects.getString("email")
                val profileImageUrl = dataObjects.getString("profileImageUrl")
                val accessToken = dataObjects.getString("accessToken")
                val refreshToken = dataObjects.getString("refreshToken")
                val loginRes = LoginRes(id, nickname, email, profileImageUrl, accessToken, refreshToken)
                Log.d("loginRes", "${loginRes}")
                loginInstance.value = loginRes

                Log.i("로그인성공", "로그인이 정상적으로 처리되었습니다.")
            }else {
                Log.e("로그인실패", "${response.code()}: ${response.message()}")
            }

        }
    }


}