package com.aboutcapsule.android.model

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aboutcapsule.android.data.mypage.GetMyPageRes
import com.aboutcapsule.android.repository.mypage.MypageRepo
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class MyPageViewModel(private val repository : MypageRepo) : ViewModel() {
    val myPageList : MutableLiveData<GetMyPageRes> = MutableLiveData()

    fun getMyPage(memberId:Int) {
        viewModelScope.launch {
            val response = repository.getMyPage(memberId)
            Log.i("res", "resresres")
            if (response.isSuccessful) {
                val jsonString = response.body()?.string()
                val jsonObject = JSONObject(jsonString)
                val dataArray = jsonObject.getJSONObject("data")
                Log.i("myPageLoadSuccess", "마이페이지 데이터를 성공적으로 받았습니다.: ${dataArray}}")
            }else {
                Log.e("myPageLoadFail", "마이페이지를 불러오지 못했습니다.")
            }
            Log.i("viewModelScope", "${response}")
        }
    }
    protected val exceptionHandler = CoroutineExceptionHandler(){ i, exception ->
        Log.d("ERR ::::", "에러 발생.... ${exception.message}");
        Log.d("ERR ::::", "에러 발생.... ${exception.toString()}");


    }
}