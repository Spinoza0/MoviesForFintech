package com.spinoza.moviesforfintech.data.network.model

import com.google.gson.annotations.SerializedName

data class FilmDto(
    @SerializedName("filmId")
    val filmId: Int? = null,

    @SerializedName("nameRu")
    val nameRu: String? = null,

    @SerializedName("nameEn")
    val nameEn: String? = null,

    @SerializedName("year")
    val year: String? = null,

    @SerializedName("filmLength")
    val filmLength: String? = null,

    @SerializedName("countries")
    val countries: List<CountryDto>? = null,

    @SerializedName("genres")
    val genres: List<GenreDto>? = null,

    @SerializedName("rating")
    val rating: String? = null,

    @SerializedName("ratingVoteCount")
    val ratingVoteCount: Int? = null,

    @SerializedName("posterUrl")
    val posterUrl: String? = null,

    @SerializedName("posterUrlPreview")
    val posterUrlPreview: String? = null,

    @SerializedName("ratingChange")
    val ratingChange: Any? = null,
)