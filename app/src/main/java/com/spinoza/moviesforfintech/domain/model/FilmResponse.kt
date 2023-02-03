package com.spinoza.moviesforfintech.domain.model

data class FilmResponse(
    val pagesCount: Int,
    val films: List<Film>?,
)