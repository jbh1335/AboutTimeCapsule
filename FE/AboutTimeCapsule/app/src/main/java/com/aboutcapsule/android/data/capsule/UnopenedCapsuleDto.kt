package com.aboutcapsule.android.data.capsule

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class UnopenedCapsuleDto(
    var capsuleId : Int,

    var openDate : String,

    var address : String,

    var isLocked : Boolean

)
