package com.aboutcapsule.android.data

data class GetCapsuleNearRes(
    val capsuleId: Int,
    val isLocked: Boolean,
    val isMine: Boolean,
    val isAllowedDistance: Boolean,
    val latitude:Double,
    val longitude:Double,
)
