package com.aboutcapsule.android.data.memory

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class MemoryRegistReq(
    val memberId: Int,
    val capsuleId: Int,
    val title: String,
    val content: String,
    val openDate: String,
)
