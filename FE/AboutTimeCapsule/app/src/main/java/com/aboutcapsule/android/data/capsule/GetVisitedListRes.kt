package com.aboutcapsule.android.data.capsule

import com.google.gson.annotations.SerializedName

data class GetVisitedListRes(

    @SerializedName("opened_capsule_dto_list")
    var openedCapsuleDtoList : MutableList<OpenedCapsuleDto> ,

    @SerializedName("map_info_dto_list")
    var mapInfoDtoList : MutableList<MapInfoDto>


)
