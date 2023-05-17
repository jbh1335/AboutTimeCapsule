package com.aboutcapsule.android.data.capsule

import com.google.gson.annotations.SerializedName

data class PostRegistCapsuleReq(

    var memberIdList : ArrayList<Int>,

    var title : String ,

    var rangeType : String ,

    var isGroup : Boolean ,

    var latitude : Double,

    var longitude : Double,

    var address : String
)
