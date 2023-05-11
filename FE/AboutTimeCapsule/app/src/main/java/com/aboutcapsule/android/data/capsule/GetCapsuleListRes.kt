package com.aboutcapsule.android.data.capsule

import com.google.gson.annotations.SerializedName

data class GetCapsuleListRes(

    @SerializedName("unopened_capsule_dto_list")
    var unopenedCapsuleDtoList : MutableList<UnopenedCapsuleDto>,

    @SerializedName("opened_capsule_dto_list")
    var openedCapsuleDtoList : MutableList<OpenedCapsuleDto>,

    @SerializedName("map_info_dto_list")
    var mapInfoDtoList : MutableList<MapInfoDto>
)
