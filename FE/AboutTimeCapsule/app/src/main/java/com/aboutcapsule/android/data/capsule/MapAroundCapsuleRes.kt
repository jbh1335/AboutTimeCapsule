package com.aboutcapsule.android.data.capsule

import com.google.gson.annotations.SerializedName

data class MapAroundCapsuleRes(
    var capsuleId : Int,

    var isLocked : Boolean,

    var isMine : Boolean,

    var isAllowedDistance : Boolean,

    var latitude : Double ,

    var longitude : Double
)
