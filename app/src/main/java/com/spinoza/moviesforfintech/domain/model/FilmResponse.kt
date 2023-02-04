package com.spinoza.moviesforfintech.domain.model

data class FilmResponse(
    val error: String,
    val films: List<Film>,
)