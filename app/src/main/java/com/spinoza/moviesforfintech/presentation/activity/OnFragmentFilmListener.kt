package com.spinoza.moviesforfintech.presentation.activity

import com.spinoza.moviesforfintech.domain.model.Film

interface OnFragmentFilmListener {
    fun saveFilm(film: Film)
}