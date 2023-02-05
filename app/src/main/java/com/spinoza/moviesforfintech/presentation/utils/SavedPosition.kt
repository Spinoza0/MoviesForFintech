package com.spinoza.moviesforfintech.presentation.utils

import android.os.Parcelable
import com.spinoza.moviesforfintech.domain.repository.ScreenType
import kotlinx.parcelize.Parcelize

@Parcelize
data class SavedPosition(
    var screenType: ScreenType,
    var position: Int,
    var needRestore: Boolean,
    var openDetails: Boolean,
) : Parcelable
