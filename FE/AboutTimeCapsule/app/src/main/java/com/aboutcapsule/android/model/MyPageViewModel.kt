package com.aboutcapsule.android.model

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aboutcapsule.android.data.mypage.FriendDtoList
import com.aboutcapsule.android.data.mypage.FriendRequestDtoList
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
            if (response.isSuccessful) {
                val jsonString = response.body()?.string()
                val jsonObject = JSONObject(jsonString)
                val dataObjects = jsonObject.getJSONObject("data")

                val nickname = dataObjects.getString("nickname")
                val email = dataObjects.getString("email")
                val profileImageUrl = dataObjects.getString("profileImageUrl")
                val friendCnt = dataObjects.getInt("friendCnt")
                val friendRequestCnt = dataObjects.getInt("friendRequestCnt")

                val friendDtoListData = dataObjects.getJSONArray("friendDtoList")
                val friendList = mutableListOf<FriendDtoList>()
                for (i in 0 until friendDtoListData.length()) {
                    val friendDtoListObject = friendDtoListData.getJSONObject(i)
                    val friendMemberId = friendDtoListObject.getInt("friendMemberId")
                    val nickname = friendDtoListObject.getString("nickname")
                    val profileImageurl = friendDtoListObject.getString("profileImageUrl")
                    friendList.add(FriendDtoList(friendMemberId, nickname, profileImageurl))
                }

                val friendRequestDtoListData = dataObjects.getJSONArray("friendRequestDtoList")

                val friendRequestList = mutableListOf<FriendRequestDtoList>()
                for (j in 0 until friendRequestDtoListData.length()) {
                    val friendReqeustDtoListObject = friendRequestDtoListData.getJSONObject(j)
                    val friendId = friendReqeustDtoListObject.getInt("friendId")
                    val friendMemeberId = friendReqeustDtoListObject.getInt("friendMemberId")
                    val nickname = friendReqeustDtoListObject.getString("nickname")
                    val profileImageUrl = friendReqeustDtoListObject.getString("profileImageUrl")
                    friendRequestList.add(FriendRequestDtoList(friendId, friendMemeberId, nickname, profileImageUrl))
                }
                val getMyPageRes: GetMyPageRes = GetMyPageRes(nickname, email, profileImageUrl, friendRequestCnt,friendCnt, friendList, friendRequestList)
                 myPageList.value = getMyPageRes
                Log.i("myPageLoadSuccess", "마이페이지 데이터를 성공적으로 받았습니다.: ${dataObjects}}")
            }else {
                Log.e("myPageLoadFail", "마이페이지를 불러오지 못했습니다.")
            }
        }
    }
    protected val exceptionHandler = CoroutineExceptionHandler(){ i, exception ->
        Log.d("ERR ::::", "에러 발생.... ${exception.message}");
        Log.d("ERR ::::", "에러 발생.... ${exception.toString()}");


    }
}