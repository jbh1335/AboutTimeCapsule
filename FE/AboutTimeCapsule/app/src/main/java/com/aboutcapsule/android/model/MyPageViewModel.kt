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

class MyPageViewModel(private val repository : MypageRepo) : ViewModel() {
    val myPageList : MutableLiveData<GetMyPageRes> = MutableLiveData()

    fun getMyPage(memberId:Int) {
        viewModelScope.launch {
            val response = repository.getMyPage(memberId)

        }
    }
    protected val exceptionHandler = CoroutineExceptionHandler(){ i, exception ->
        Log.d("ERR ::::", "에러 발생.... ${exception.message}");
        Log.d("ERR ::::", "에러 발생.... ${exception.toString()}");


    }
}