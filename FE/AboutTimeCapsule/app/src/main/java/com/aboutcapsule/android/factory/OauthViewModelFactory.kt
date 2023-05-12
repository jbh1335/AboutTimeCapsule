package com.aboutcapsule.android.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aboutcapsule.android.model.OauthViewModel
import com.aboutcapsule.android.repository.OauthRepo

class OauthViewModelFactory(
    private val repository: OauthRepo
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OauthViewModel(repository) as T
    }
}
