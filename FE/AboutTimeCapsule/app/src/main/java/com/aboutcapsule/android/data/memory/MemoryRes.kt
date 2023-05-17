package com.aboutcapsule.android.data.memory

data class MemoryRes(
    val capsuleTitle: String,
    val isGroup: Boolean,
    val rangeType:String,
    val address: String,
    val isFirstGroup: Boolean,
    val isCapsuleMine: Boolean,
    val memoryDetailDtoList: List<MemoryDetailDto>
)
