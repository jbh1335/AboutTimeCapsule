package com.aboutcapsule.android.data.capsule

import com.google.gson.annotations.SerializedName

data class MapInfoDto(

    @SerializedName("capsule_id")
    var capsuleId : Int,

    var latitude : Double,

    var longitude : Double,

    @SerializedName("is_opened")
    var isOpened : Boolean,

    @SerializedName("is_locked")
    var isLocked : Boolean

)
