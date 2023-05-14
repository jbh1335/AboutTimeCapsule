package com.aboutcapsule.android.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aboutcapsule.android.data.mypage.AllFriendRes
import com.aboutcapsule.android.data.mypage.FriendDtoList
import com.aboutcapsule.android.data.mypage.FriendRequestDtoList
import com.aboutcapsule.android.data.mypage.GetMyPageRes
import com.aboutcapsule.android.repository.MypageRepo
import com.aboutcapsule.android.util.GlobalAplication
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import org.json.JSONObject

class MyPageViewModel(private val repository : MypageRepo) : ViewModel() {
    var currentUser = GlobalAplication.preferences.getInt("currentUser", -1)
    var myPageList : MutableLiveData<GetMyPageRes> = MutableLiveData()
    lateinit var friendList : MutableList<FriendDtoList>
    lateinit var friendRequestList : MutableList<FriendRequestDtoList>
    var checkNickname: MutableLiveData<Boolean> = MutableLiveData()
    var isModifyNickname: MutableLiveData<Boolean> = MutableLiveData()
    var allFriendList : MutableLiveData<MutableList<AllFriendRes>> = MutableLiveData()
    var friendId :Int? = 0
    var friendReqId: Int = 0


    fun getMyPage(currentUser:Int, memberId:Int?) {
        viewModelScope.launch {
            friendId = memberId
            val response = repository.getMyPage(currentUser, memberId!!)
            if (response.isSuccessful) {
                val jsonString = response.body()?.string()
                val jsonObject = JSONObject(jsonString)
                val dataObjects = jsonObject.getJSONObject("data")
                val friendDataId = dataObjects.getInt("friendId")
                val otherMemberId = dataObjects.getInt("otherMemberId")
                friendReqId = friendDataId
                val state = dataObjects.getString("state")
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
                Log.d("friendRequestList in viewmodel", "${friendRequestList}")
                val getMyPageRes = GetMyPageRes(friendDataId, otherMemberId, state,nickname, email, profileImageUrl, friendRequestCnt,friendCnt, friendList, friendRequestList)
                 myPageList.value = getMyPageRes

                Log.i("myPageLoadSuccess", "마이페이지 데이터를 성공적으로 받았습니다.: ${dataObjects}}")
            }else {
                Log.e("myPageLoadFail", "마이페이지를 불러오지 못했습니다.")
            }
        }
    }
    fun friendAcceptRequest(currentUser: Int, friendReqId: Int?) {
        viewModelScope.launch {
            val response = repository.acceptFriendRequest(friendReqId)
            if (response.isSuccessful) {
                getMyPage(currentUser, friendId)
                Log.i("친구요청", "친구 요청 성공 ${response.body()?.string()}")
            }else {
                Log.e("친구요청실패", "친구 요청 실패")
            }

        }

    }
    fun refuseFriendRequest(currentUser: Int, friendReqId:Int) {
        viewModelScope.launch {
            val response = repository.refuseFriendRequest(friendReqId)
            if (response.isSuccessful) {
                getMyPage(currentUser, friendId)
                Log.i("친구요청거절", "친구 요청거절 성공")
            } else {
                Log.e("친구요청거절실패", "친구 요청거절 실패")
            }
        }
    }
    fun checkNickname(nickname: String) {
        viewModelScope.launch {
            val response = repository.checkNickname(nickname)
            Log.d("여긴 오니?", "ㅎㅇㄱ")
            if (response.isSuccessful) {
                val jsonString = response.body()?.string()
                val jsonObject = JSONObject(jsonString)
                val nicknameBoolean = jsonObject.getString("data").toBoolean()
                Log.d("닉네임체크", "${nicknameBoolean}")
                checkNickname.value = nicknameBoolean
            } else {
                Log.e("에러창인데", "여기야?")
            }
        }
    }
    fun modifyNickname(memberId: Int, nickname: String) {
        viewModelScope.launch {
            val response = repository.modifyNickname(memberId, nickname)
            if (response.isSuccessful) {
                val jsonString = response.body()?.string()
                val jsonObject = JSONObject(jsonString)
                val isModifyNicknameData = jsonObject.getString("success").toBoolean()
                isModifyNickname.value = isModifyNicknameData
                Log.d("닉네임변경요청api쏘는중", "${isModifyNicknameData}")
            }
        }
    }
    fun getMyAllFriendList(memberId: Int?) {
        viewModelScope.launch {
            Log.d("모든친구불러오기멤버아이디", "${memberId}")
            val response = repository.getMyAllFriend(memberId)
            if (response.isSuccessful) {
                val jsonString = response.body()?.string()
                val jsonObject = JSONObject(jsonString)
                val dataArray = jsonObject.getJSONArray("data")
                var dataList = mutableListOf<AllFriendRes>()
                for (i in 0 until dataArray.length()) {
                    val dataObject = dataArray.getJSONObject(i)
                    val friendId = dataObject.getInt("friendId")
                    val friendMemberId = dataObject.getInt("friendMemberId")
                    val nickname = dataObject.getString("nickname")
                    val profileImageUrl = dataObject.getString("profileImageUrl")
                    dataList.add(AllFriendRes(friendId, friendMemberId, nickname, profileImageUrl))
                }
                Log.d("모든친구불러오기", "${dataList}")
                allFriendList.value = dataList

            }else {
                Log.e("모든친구불러오기실패", "${response.code()}, ${response.message()}")
            }
        }
    }

    fun sendFriendRequest(fromMemberId: Int, toMemberId: Int) {
        viewModelScope.launch {
            val response = repository.sendFriendRequest(fromMemberId, toMemberId)
            if (response.isSuccessful) {
                getMyPage(fromMemberId, toMemberId)
            }
        }
    }

    fun deleteFriend(friendReqId: Int) {
        viewModelScope.launch {
            val response = repository.deleteFriend(friendReqId)
            Log.d("친구삭제", "${response.message()}")
            Log.d("friendReqId", "${friendReqId}")
            if (response.isSuccessful) {
                getMyPage(currentUser, friendId)
            }
        }
    }


    protected val exceptionHandler = CoroutineExceptionHandler(){ i, exception ->
        Log.d("ERR ::::", "에러 발생.... ${exception.message}");
        Log.d("ERR ::::", "에러 발생.... ${exception.toString()}");


    }
}