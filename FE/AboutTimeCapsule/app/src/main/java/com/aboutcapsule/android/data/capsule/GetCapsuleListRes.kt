package com.aboutcapsule.android.data.capsule

data class GetCapsuleListRes(

    var unopenedCapsuleDtoList : MutableList<UnopenedCapsuleDto>,

    var openedCapsuleDtoList : MutableList<OpenedCapsuleDto>,

    var mapInfoDtoList : MutableList<MapInfoDto>
)
