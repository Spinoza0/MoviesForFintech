package com.spinoza.moviesforfintech.domain.model

sealed class FilmsState {
    object Loading : FilmsState()
    class Error(val value: String) : FilmsState()
    class AllFilms(val value: List<Film>) : FilmsState()
    class OneFilm(val value: Film) : FilmsState()
}