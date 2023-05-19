package com.aboutcapsule.android.data.memory

data class PostCommentReq(
    val memoryId: Int,
    val memberId: Int,
    val content: String,
)
