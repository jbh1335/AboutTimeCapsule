package com.aboutcapsule.android.data.capsule

import com.google.gson.annotations.SerializedName

data class GetVisitedListRes(

    var openedCapsuleDtoList : MutableList<OpenedCapsuleDto> ,

    var mapInfoDtoList : MutableList<MapInfoDto>


)
