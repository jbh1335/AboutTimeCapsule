package com.aboutcapsule.android.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aboutcapsule.android.model.MyPageViewModel
import com.aboutcapsule.android.repository.mypage.MypageRepo

class MyPageViewModelFactory(
    private val repository: MypageRepo
    ) :ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MyPageViewModel(repository) as T
    }
}


