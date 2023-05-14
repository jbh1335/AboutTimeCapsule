package com.aboutcapsule.android.data.mypage

data class GetOtherPeoplePageRes(
    val friendId: Int,
    val memberId: Int,
    val state: String,
    val nickname: String,
    val email: String,
    val profileImageUrl: String,
    val friendCnt: Int,
    val friendDtoList: MutableList<FriendDtoList>
)
