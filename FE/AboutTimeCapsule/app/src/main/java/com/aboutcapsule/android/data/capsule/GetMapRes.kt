package com.aboutcapsule.android.data.capsule

import com.google.gson.annotations.SerializedName

data class GetMapRes(
    @SerializedName("data")
    var mapDtoList : MutableList<MapDto>
)
