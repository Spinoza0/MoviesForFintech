package com.spinoza.moviesforfintech.presentation.utils

import com.spinoza.moviesforfintech.domain.repository.SourceType

data class SavedPosition(
    val sourceType: SourceType,
    var position: Int,
    var needRestore: Boolean,
)
