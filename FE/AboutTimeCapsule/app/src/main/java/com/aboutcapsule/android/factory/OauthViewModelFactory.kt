package com.aboutcapsule.android.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aboutcapsule.android.model.MyPageViewModel
import com.aboutcapsule.android.model.OauthViewModel
import com.aboutcapsule.android.repository.mypage.MypageRepo
import com.aboutcapsule.android.repository.oauth.OauthRepo

class OauthViewModelFactory(
    private val repository: OauthRepo
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OauthViewModel(repository) as T
    }
}
