package com.spinoza.moviesforfintech.domain.model

data class FilmResponse(
    val pagesCount: Int,
    val error: String,
    val films: List<Film>,
)