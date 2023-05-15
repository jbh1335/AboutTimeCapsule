package com.aboutcapsule.android.data.capsule

import com.google.gson.annotations.SerializedName

data class PostRegistCapsuleReq(

    @SerializedName("member_id_list")
    var memberIdList : MutableList<Integer>,

    var title : String ,

    @SerializedName("range_type")
    var rangeType : String ,

    @SerializedName("is_group")
    var isGroup : Boolean ,

    var latitude : Double,

    var longitude : Double,

    var address : String
)
