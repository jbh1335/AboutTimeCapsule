package com.aboutcapsule.android.data.capsule

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class UnopenedCapsuleDto(
    @SerializedName("capsule_id")
    var capsuleId : Int,

    @SerializedName("open_date")
    var openDate : LocalDate,

    var address : String,

    @SerializedName("is_locked")
    var isLocked : Boolean

)
