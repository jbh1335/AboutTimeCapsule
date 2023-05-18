package com.aboutcapsule.android.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aboutcapsule.android.data.memory.CommentRes
import com.aboutcapsule.android.data.memory.GroupOpenDateReq
import com.aboutcapsule.android.data.memory.MemoryDetailDto
import com.aboutcapsule.android.data.memory.MemoryReq
import com.aboutcapsule.android.data.memory.MemoryRes
import com.aboutcapsule.android.data.memory.PostCommentReq
import com.aboutcapsule.android.repository.MemoryRepo
import com.aboutcapsule.android.views.capsule.ArticleRegistFragment
import com.aboutcapsule.android.views.capsule.CommentsData
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.http.Body
import java.time.LocalDate

class MemoryViewModel(val repository : MemoryRepo) : ViewModel() {
    var MemoryResData: MutableLiveData<MemoryRes> = MutableLiveData()
    var memoryComments: MutableLiveData<MutableList<CommentRes>> = MutableLiveData()

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

    fun getCapsuleMemory(memoryReq: MemoryReq) {
        viewModelScope.launch {
            val response = repository.getCapsuleMemory(memoryReq)

            if (response.isSuccessful) {
                val jsonString = response.body()?.string()
                Log.d("겟캡슐메모리", "$jsonString")
                val jsonObject = JSONObject(jsonString)
                val dataObjects = jsonObject.getJSONObject("data")
                val capsuleTitle = dataObjects.getString("capsuleTitle")
                val isGroup= dataObjects.getBoolean("isGroup")
                val rangeType= dataObjects.getString("rangeType")
                val address= dataObjects.getString("address")
                val isFirstGroup= dataObjects.getBoolean("isFirstGroup")
                val isCapsuleMine= dataObjects.getBoolean("isCapsuleMine")
                val memoryDetailDtoList = dataObjects.getJSONArray("memoryDetailDtoList")
                val memoryDetailDtoListData = ArrayList<MemoryDetailDto>()
                for (i in 0 until memoryDetailDtoList.length()) {
                    val getArray = memoryDetailDtoList.getJSONObject(i)
                    val memoryId = getArray.getInt("memoryId")
                    val nickname= getArray.getString("nickname")
                    val memoryTitle= getArray.getString("memoryTitle")
                    val profileImageUrl= getArray.getString("profileImageUrl")
                    val content= getArray.getString("content")
                    val imageUrlJson= getArray.getJSONArray("imageUrl")
                    val imageUrlData = ArrayList<String>()
                    for (j in 0 until imageUrlJson.length()) {
                        val imageUrl = imageUrlJson.getString(j)
                        imageUrlData.add(imageUrl)
                    }
                    val commentCnt= getArray.getInt("commentCnt")
                    val createdDate= getArray.getString("createdDate")
                    val createdDateToLocalDate = LocalDate.parse(createdDate)
                    Log.d("createdDate", "${createdDate}")
                    val isLocked= getArray.getBoolean("isLocked")
                    val isOpend= getArray.getBoolean("isOpend")
                    val isMemoryMine= getArray.getBoolean("isMemoryMine")
                    val memoryDetail = MemoryDetailDto(memoryId,nickname,memoryTitle,profileImageUrl,content,
                            imageUrlData,commentCnt, createdDateToLocalDate, isLocked, isOpend, isMemoryMine)
                    memoryDetailDtoListData.add(memoryDetail)
                }
                val memoryRes = MemoryRes(capsuleTitle,isGroup, rangeType, address,
                    isFirstGroup, isCapsuleMine, memoryDetailDtoListData)
                MemoryResData.value = memoryRes
            }else {
                Log.d("getCapsuleMemory", "${response.code()} / ${response.message()}")
            }
        }
    }

    fun getMemoryComments(memoryId: Int) {
        viewModelScope.launch {
            val response = repository.getMemoryComments(memoryId)
            if (response.isSuccessful) {
                val jsonString = response.body()?.string()
                val jsonObject = JSONObject(jsonString)
                val dataArrays = jsonObject.getJSONArray("data")
                val commentResList = mutableListOf<CommentRes>()
                for (i in 0 until dataArrays.length()) {
                    val commentResObject = dataArrays.getJSONObject(i)
                    val commentId = commentResObject.getInt("commentId")
                    val memberId = commentResObject.getInt("memberId")
                    val nickname = commentResObject.getString("nickname")
                    val profileImageUrl = commentResObject.getString("profileImageUrl")
                    val content = commentResObject.getString("content")
                    val createdDate = commentResObject.getString("createdDate")
                    val commentRes = CommentRes(commentId, memberId, nickname,
                        profileImageUrl, content, createdDate)
                    commentResList.add(commentRes)
                }
                memoryComments.value = commentResList
            }
        }
    }
    fun postMemoryComment(postCommentReq: PostCommentReq) {
        viewModelScope.launch {
            val response = repository.postMemoryComment(postCommentReq)
            if (response.isSuccessful) {
                Log.d("댓글 등록 성공", "${response.code()} / ${response.message()}")
            } else {
                Log.d("댓글 등록 실패", "${response.code()} / ${response.message()}")
            }
        }
    }
    fun sealMemoryFirst(groupOpenDateReq: GroupOpenDateReq) {
        viewModelScope.launch {
            Log.d("추억등록 데이터 확인", "${groupOpenDateReq}")
            Log.d("추억등록 데이터 확인", "${groupOpenDateReq.openDate}")
            val response = repository.sealMemoryFirst(groupOpenDateReq)
            if (response.isSuccessful) {
                Log.d("추억 등록 성공", "${response.code()} / ${response.message()}")
            } else {
                Log.d("댓글 등록 실패", "${response.code()} / ${response.message()}")
            }
        }
    }


}