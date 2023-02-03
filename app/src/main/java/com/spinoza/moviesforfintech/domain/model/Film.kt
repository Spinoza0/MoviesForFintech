package com.spinoza.moviesforfintech.domain.model

data class Film(
    val filmId: Int? = null,
    val nameRu: String? = null,
    val year: Int? = null,
    val countries: List<String>,
    val genres: List<String>,
    val posterUrl: String? = null,
    val posterUrlPreview: String? = null,
    val description: String? = null,
)