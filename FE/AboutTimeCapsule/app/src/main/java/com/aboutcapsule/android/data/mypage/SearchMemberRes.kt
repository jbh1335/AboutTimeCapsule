package com.aboutcapsule.android.data.mypage

import com.google.gson.annotations.SerializedName

data class SearchMemberRes(
    @SerializedName("friend_id")
    val friendId: Int,
    @SerializedName("member_id")
    val memberId: Int,
    val state: String,
    val nickname: String,
    @SerializedName("profile_image")
    val profileImage: String,
)
