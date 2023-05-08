package com.aboutcapsule.android.data.mypage

import com.google.gson.annotations.SerializedName

data class FriendRequestDtoList(
    @SerializedName("friend_id")
    val friendId:Int,
    @SerializedName("friend_member_id")
    val friendMemberId: Int,

    val nickname:String,
    @SerializedName("profile_image_url")
    val profileImageUrl:String
)
