package com.aboutcapsule.android.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aboutcapsule.android.model.AlarmViewModel
import com.aboutcapsule.android.model.CapsuleViewModel
import com.aboutcapsule.android.repository.AlarmRepo
import com.aboutcapsule.android.repository.CapsuleRepo

class AlarmViewModelFactory (private val repository : AlarmRepo) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AlarmViewModel(repository) as T
    }
}