package com.aboutcapsule.android.data.capsule

import com.google.gson.annotations.SerializedName

data class GetFriendListRes(

    @SerializedName("data")
    var friendList : MutableList<FriendDto>

)
