package com.aboutcapsule.android.data.capsule

import com.google.gson.annotations.SerializedName

data class FriendDto(

    @SerializedName("member_id")
    var memberId : Int,

    var nickname : String,

    @SerializedName("profile_image_url")
    var profileImageUrl : String

)
