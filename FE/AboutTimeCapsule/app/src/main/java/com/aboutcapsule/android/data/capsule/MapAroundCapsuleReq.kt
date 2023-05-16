package com.aboutcapsule.android.data.capsule

import com.google.gson.annotations.SerializedName

data class MapAroundCapsuleReq(
    @SerializedName("member_id")
    var memberId : Int ,

    var latitude : Double ,

    var longitude : Double
)
