package com.aboutcapsule.android.data.capsule

import com.google.gson.annotations.SerializedName

data class GetGroupMemberRes(
    @SerializedName("data")
    var groupMemberList : MutableList<GroupMemberDto>
)