package com.aboutcapsule.android.data.capsule

import com.google.gson.annotations.SerializedName

data class GetAroundCapsuleListRes(
    @SerializedName("data")
    var aroundCapsuleDtoList : MutableList<AroundCapsuleDto>
)
