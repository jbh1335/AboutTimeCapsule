package com.aboutcapsule.android.data.oauth

import com.google.gson.annotations.SerializedName

data class LoginRes(

    val id:Int,
    val nickname:String,
    val email:String,
    @SerializedName("profile_img_url")
    val profileImgUrl:String,
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("refresh_token")
    val refreshToken: String
)
