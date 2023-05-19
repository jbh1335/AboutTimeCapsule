package com.aboutcapsule.android.data.capsule

import java.time.LocalDate

data class PostMapCapsuleDetailRes(
    var capsuleId : Int ,
    var memberNickname : String ,
    var leftTime : String ,
    var isLocked : Boolean,
    var isGroup : Boolean,
    var openDate : String
)
