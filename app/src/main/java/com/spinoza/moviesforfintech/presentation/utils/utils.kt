package com.spinoza.moviesforfintech.presentation.utils

import android.os.Build
import android.os.Bundle
import com.spinoza.moviesforfintech.domain.model.Film
import com.spinoza.moviesforfintech.domain.repository.ScreenType

const val KEY_SAVED_POSITION = "savedPosition"
const val KEY_SAVED_FILM = "film"

fun getSavedPositionFromBundle(bundle: Bundle?): SavedPosition {
    var result = SavedPosition(
        screenType = ScreenType.POPULAR,
        position = 0,
        needRestore = false,
        openDetails = false
    )
    bundle?.let {
        val type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            it.getParcelable(KEY_SAVED_POSITION, SavedPosition::class.java)
        } else {
            @Suppress("deprecation")
            it.getParcelable(KEY_SAVED_POSITION)
        }
        type?.let { value ->
            result = value
        }
    }
    return result
}

fun getFilmFromBundle(bundle: Bundle?): Film {
    bundle?.let {
        val film = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            it.getParcelable(KEY_SAVED_FILM, Film::class.java)
        } else {
            @Suppress("deprecation")
            it.getParcelable(KEY_SAVED_FILM)
        }
        film?.let { value ->
            return value
        }
    }
    throw RuntimeException("Parameter FILM not found in bundle")
}
