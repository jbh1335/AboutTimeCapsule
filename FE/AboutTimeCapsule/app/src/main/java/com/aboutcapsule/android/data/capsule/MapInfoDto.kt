package com.aboutcapsule.android.data.capsule

import com.google.gson.annotations.SerializedName

data class MapInfoDto(

    var capsuleId : Int,

    var latitude : Double,

    var longitude : Double,

    var isOpened : Boolean,

    var isLocked : Boolean

)
