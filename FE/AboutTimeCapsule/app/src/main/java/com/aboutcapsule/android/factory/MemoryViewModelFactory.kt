package com.aboutcapsule.android.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aboutcapsule.android.model.MemoryViewModel
import com.aboutcapsule.android.repository.MemoryRepo


class MemoryViewModelFactory ( private val repository : MemoryRepo) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MemoryViewModel(repository) as T
    }
}
