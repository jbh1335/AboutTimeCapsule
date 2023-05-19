package com.aboutcapsule.android.data.capsule

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class OpenedCapsuleDto (

    @SerializedName("capsule_id")
    var capsuleId : Int ,

    @SerializedName("open_date")
    var openDate : String,

    var address : String ,

    @SerializedName("is_added")
    var isAdded : Boolean

)