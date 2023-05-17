package com.aboutcapsule.android.data.memory

import java.time.LocalDate

data class MemoryDetailDto(
    val memoryId: Int,
    val nickname: Int,
    val memoryTitle: String,
    val profileImageUrl: String,
    val content: String,
    val imageUrl: Array<String>,
    val commentCnt: Int,
    val createdDate: LocalDate,
    val isLocked: Boolean,
    val isOpend: Boolean,
    val isMemoryMine: Boolean,
    )
