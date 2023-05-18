package com.aboutcapsule.android.data.memory

data class CommentRes(
    val commentId: Int,
    val memberId: Int,
    val nickname: String,
    val profileImageUrl: String,
    val content: String,
    val createdDate: String,
)
