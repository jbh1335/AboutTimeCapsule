package com.aboutcapsule.android.data.oauth

data class LoginRes(

    val id:Int,
    val nickname:String,
    val email:String,
    val profileImgUrl:String,
    val accessToken: String,
    val refreshToken: String
)
