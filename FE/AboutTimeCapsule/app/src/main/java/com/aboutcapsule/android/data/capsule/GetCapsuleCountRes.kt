package com.aboutcapsule.android.data.capsule

import com.google.gson.annotations.SerializedName

data class GetCapsuleCountRes(
    @SerializedName("data")
    var capsuleCountRes : CapsuleCountRes
)
