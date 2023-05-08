package com.aboutcapsule.android.data.mypage

import com.google.gson.annotations.SerializedName

data class FriendDtoList(
    @SerializedName("friend_member_id")
    val friendMemberId:Int,
    val nickname:String,
    @SerializedName("profile_img_url")
    val profileImageUrl:String
)
