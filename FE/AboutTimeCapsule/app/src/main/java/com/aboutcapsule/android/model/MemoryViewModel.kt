package com.aboutcapsule.android.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aboutcapsule.android.data.memory.MemoryRegistReq
import com.aboutcapsule.android.repository.MemoryRepo
import com.aboutcapsule.android.views.capsule.ArticleRegistFragment
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class MemoryViewModel(val repository : MemoryRepo) : ViewModel() {

    companion object {

    }

    fun registerMemory(imageList: ArrayList<MultipartBody.Part>,memoryRegistReq: RequestBody) {
        viewModelScope.launch {
            val response = repository.registerMemory(imageList, memoryRegistReq)
            Log.d("viewModel", "${ArticleRegistFragment.imageList}")
            Log.d("viewModelreq", "${memoryRegistReq}")
            if (response.isSuccessful) {
                Log.d("response", "${response.code()}")
            } else {
                Log.e("추억등록실패", "${response.code()} / ${response.message()}")
            }
        }
    }


}