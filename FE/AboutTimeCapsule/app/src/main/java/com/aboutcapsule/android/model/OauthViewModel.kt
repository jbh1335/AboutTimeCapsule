package com.aboutcapsule.android.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aboutcapsule.android.data.oauth.LoginRes
import com.aboutcapsule.android.repository.oauth.OauthRepo
import kotlinx.coroutines.launch
import org.json.JSONObject

class OauthViewModel(private val repository: OauthRepo) : ViewModel() {
    val loginInstance : MutableLiveData<LoginRes> = MutableLiveData()

    fun doLogin(provider:String, accessToken:String) {
        viewModelScope.launch {
            val response = repository.doLogin(provider, accessToken)
            if(response.code().equals(200)) {
                val jsonString = response.body()?.string()
                val jsonObject = JSONObject(jsonString)
                val dataObjects = jsonObject.getJSONObject("data")


                Log.i("로그인성공", "로그인이 정상적으로 처리되었습니다.")
            } else if(response.code().equals("새로운회원")) {

            }else {
                Log.e("로그인실패", "${response.code()}: ${response.message()}")
            }

        }
    }


}