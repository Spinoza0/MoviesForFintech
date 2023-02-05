package com.spinoza.moviesforfintech.domain.repository

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class ScreenType : Parcelable {
    WITHOUT_TYPE,
    POPULAR,
    FAVOURITE,
}