package com.spinoza.moviesforfintech.domain.model

data class Film(
    val filmId: Int,
    val nameRu: String? = null,
    val year: String? = null,
    val countries: List<Country>? = null,
    val genres: List<Genre>? = null,
    val posterUrl: String? = null,
)