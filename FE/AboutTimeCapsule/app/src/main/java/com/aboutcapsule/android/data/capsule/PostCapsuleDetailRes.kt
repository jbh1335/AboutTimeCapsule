package com.aboutcapsule.android.data.capsule

data class PostCapsuleDetailRes(
    var capsuleId : Int,
    var memberNickname : String,
    var distance : Int ,
    var leftTime : String,
    var isLocked : Boolean,
    var latitude : Double,
    var longitude : Double,
    var address : String
)
