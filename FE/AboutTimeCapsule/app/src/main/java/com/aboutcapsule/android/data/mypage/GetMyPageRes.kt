package com.aboutcapsule.android.data.mypage

import com.google.gson.annotations.SerializedName

data class GetMyPageRes(
    val nickname:String,
    val email:String,
    @SerializedName("profile_image_url")
    val profileImageUrl:String,
    @SerializedName("friend_cnt")
    val friendCnt:Int,
    @SerializedName("friend_request_cnt")
    val friendRequestCnt:Int,
    @SerializedName("friend_dto_list")
    val friendDtoList:ArrayList<FriendDtoList>,
    @SerializedName("request_dto_list")
    val requestDtoList:ArrayList<FriendRequestDtoList>

)
