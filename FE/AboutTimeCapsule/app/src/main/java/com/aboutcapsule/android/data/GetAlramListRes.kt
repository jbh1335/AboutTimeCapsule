package com.aboutcapsule.android.data

data class GetAlramListRes(
    val alarmId: Int,
    val memberId: Int,
    val capsuleId: Int,
    val content: String,
    val categoryType: String,
)
