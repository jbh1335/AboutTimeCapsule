package com.aboutcapsule.android.data.capsule

import com.google.gson.annotations.SerializedName

data class AroundCapsuleDto(
    @SerializedName("capsule_id")
    var capsuleId : Int,

    @SerializedName("member_nickname")
    var memberNickname : String,

    var address : String
)
