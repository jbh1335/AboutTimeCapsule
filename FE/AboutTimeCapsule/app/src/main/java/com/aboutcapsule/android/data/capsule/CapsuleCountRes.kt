package com.aboutcapsule.android.data.capsule

import com.google.gson.annotations.SerializedName

data class CapsuleCountRes(

    @SerializedName("my_capsule_cnt")
    var myCapsuleCnt : Int,

    @SerializedName("friend_capsule_cnt")
    var friendCapsuleCnt : Int,

    @SerializedName("open_capsule_cnt")
    var openCapsuleCnt : Int ,

    @SerializedName("is_new_alarm")
    var isNewAlarm : Boolean
)
