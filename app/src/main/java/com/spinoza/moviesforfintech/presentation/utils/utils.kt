package com.spinoza.moviesforfintech.presentation.utils

import android.os.Build
import android.os.Bundle
import com.spinoza.moviesforfintech.domain.repository.SourceType

const val SOURCE_TYPE = "sourceType"

fun getSourceTypeFromBundle(bundle: Bundle?): SourceType {
    var result = SourceType.POPULAR
    bundle?.let {
        val type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            it.getParcelable(SOURCE_TYPE, SourceType::class.java)
        } else {
            @Suppress("deprecation")
            it.getParcelable(SOURCE_TYPE)
        }
        type?.let { value ->
            result = value
        }
    }
    return result
}

