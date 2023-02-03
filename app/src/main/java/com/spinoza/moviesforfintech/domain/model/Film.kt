package com.spinoza.moviesforfintech.domain.model

data class Film(
    val filmId: Int? = null,
    val nameRu: String? = null,
    val year: String? = null,
    val countries: List<Country>,
    val genres: List<Genre>,
    val posterUrl: String? = null,
    val description: String? = null,
)