package com.aboutcapsule.android.data.capsule

import com.google.gson.annotations.SerializedName

data class MapDto(
    @SerializedName("capsule_id")
    var capsuleId : Int,

    @SerializedName("is_locked")
    var isLocked : Boolean,

    @SerializedName("is_mine")
    var isMine : Boolean,

    @SerializedName("is_allowed_distance")
    var isAllowedDistance : Boolean,

    var latitude : Double ,

    var longitude : Double
)
