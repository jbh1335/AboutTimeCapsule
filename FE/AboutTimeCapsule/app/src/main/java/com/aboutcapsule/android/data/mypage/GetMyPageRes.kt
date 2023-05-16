package com.aboutcapsule.android.data.mypage

import com.google.gson.annotations.SerializedName

data class GetMyPageRes(
    @SerializedName("friend_id")
    val friendId:Int,
    @SerializedName("other_member_id")
    val otherMemberId:Int,
    val state:String,
    val nickname:String,
    val email:String,
    @SerializedName("profile_image_url")
    val profileImageUrl:String,
    @SerializedName("friend_cnt")
    val friendCnt:Int,
    @SerializedName("friend_request_cnt")
    val friendRequestCnt:Int,
    @SerializedName("friend_dto_list")
    val friendDtoList:MutableList<FriendDtoList>,
    @SerializedName("friend_request_dto_list")
    val friendRequestDtoList:MutableList<FriendRequestDtoList>

)
