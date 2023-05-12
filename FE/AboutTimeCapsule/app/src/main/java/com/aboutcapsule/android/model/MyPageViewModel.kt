package com.aboutcapsule.android.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aboutcapsule.android.data.mypage.FriendDtoList
import com.aboutcapsule.android.data.mypage.FriendRequestDtoList
import com.aboutcapsule.android.data.mypage.GetMyPageRes
import com.aboutcapsule.android.repository.MypageRepo
import com.aboutcapsule.android.util.GlobalAplication
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject

class MyPageViewModel(private val repository : MypageRepo) : ViewModel() {
    var myPageList : MutableLiveData<GetMyPageRes> = MutableLiveData()
    lateinit var friendList : MutableList<FriendDtoList>
    lateinit var friendRequestList : MutableList<FriendRequestDtoList>
    var checkNickname: MutableLiveData<Boolean> = MutableLiveData()

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
                friendList = mutableListOf<FriendDtoList>()
                for (i in 0 until friendDtoListData.length()) {
                    val friendDtoListObject = friendDtoListData.getJSONObject(i)
                    val friendMemberId = friendDtoListObject.getInt("friendMemberId")
                    val nickname = friendDtoListObject.getString("nickname")
                    val profileImageurl = friendDtoListObject.getString("profileImageUrl")
                    friendList.add(FriendDtoList(friendMemberId, nickname, profileImageurl))
                }

                val friendRequestDtoListData = dataObjects.getJSONArray("friendRequestDtoList")

                friendRequestList = mutableListOf<FriendRequestDtoList>()
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
    fun friendAcceptRequest(friendId: Int?, memberId:Int) {
        viewModelScope.launch {
            val response = repository.acceptFriendRequest(friendId)
            if (response.isSuccessful) {
                getMyPage(memberId)
                Log.i("친구요청", "친구 요청 성공 ${response.body()?.string()}")
            }else {
                Log.e("친구요청실패", "친구 요청 실패")
            }

        }

    }
    fun refuseFriendRequest(friendId: Int?, memberId:Int) {
        viewModelScope.launch {
            val response = repository.refuseFriendRequest(friendId)
            if (response.isSuccessful) {
                getMyPage(memberId)
                Log.i("친구요청거절", "친구 요청거절 성공")
            } else {
                Log.e("친구요청거절실패", "친구 요청거절 실패")
            }
        }
    }
    fun checkNickname(nickname: String) {
        viewModelScope.launch {
            val response = repository.checkNickname(nickname)
            if (response.isSuccessful) {
                val jsonString = response.body()?.string()
                val jsonObject = JSONObject(jsonString)
                val nicknameBoolean = jsonObject.getJSONObject("data").toString().toBoolean()
                checkNickname.value = nicknameBoolean
            }
        }
    }

    protected val exceptionHandler = CoroutineExceptionHandler(){ i, exception ->
        Log.d("ERR ::::", "에러 발생.... ${exception.message}");
        Log.d("ERR ::::", "에러 발생.... ${exception.toString()}");


    }
}