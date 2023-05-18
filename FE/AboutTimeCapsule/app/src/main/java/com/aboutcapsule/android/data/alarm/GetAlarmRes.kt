package com.aboutcapsule.android.data.alarm

data class GetAlarmRes(
    var alarmId : Int,
    var memberId : Int ,
    var capsuleId : Int,
    var content : String,
    var categoryType : String
)
