package com.aboutcapsule.android.model

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aboutcapsule.android.data.mypage.AllFriendRes
import com.aboutcapsule.android.data.mypage.FriendDtoList
import com.aboutcapsule.android.data.mypage.FriendRequestDtoList
import com.aboutcapsule.android.data.mypage.GetMyPageRes
import com.aboutcapsule.android.repository.mypage.MypageRepo
import com.aboutcapsule.android.util.GlobalAplication
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response

class MyPageViewModel(private val repository : MypageRepo) : ViewModel() {
    var myPageList : MutableLiveData<GetMyPageRes> = MutableLiveData()
    lateinit var friendList : MutableList<FriendDtoList>
    lateinit var friendRequestList : MutableList<FriendRequestDtoList>
    var allFriendList : MutableLiveData<MutableList<AllFriendRes>> = MutableLiveData()
    var isCurrentUser: Boolean = false

    fun getMyPage(memberId:Int?) {
        viewModelScope.launch {
            Log.d("뷰모델겟마이페이지", "${memberId}")
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
                val getMyPageRes = GetMyPageRes(nickname, email, profileImageUrl, friendRequestCnt,friendCnt, friendList, friendRequestList)
                 myPageList.value = getMyPageRes

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
    fun getMyAllFriendList(memberId: Int?) {
        viewModelScope.launch {
            val response = repository.getMyAllFriend(memberId)

            if (response.isSuccessful) {
                val jsonString = response.body()?.string()
                Log.d("뷰모델초반", "${jsonString}")
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
                allFriendList.value = dataList
                Log.d("마이페이지 뷰모델", "여기야?")
            }else {
                Log.e("모든친구불러오기실패", "${response.code()}, ${response.message()}")
            }
        }
    }

    fun jsonParsing(response : Response<ResponseBody>) :JSONObject {
        val jsonString = response.body()?.string()
        val jsonObject = JSONObject(jsonString)
        return jsonObject


    }

    protected val exceptionHandler = CoroutineExceptionHandler(){ i, exception ->
        Log.d("ERR ::::", "에러 발생.... ${exception.message}");
        Log.d("ERR ::::", "에러 발생.... ${exception.toString()}");


    }
}