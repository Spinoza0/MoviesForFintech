package com.spinoza.moviesforfintech.data.network.model

import com.google.gson.annotations.SerializedName

data class ResponseDto(
    @SerializedName("pagesCount")
    val pagesCount: Int = 0,

    @SerializedName("films")
    val films: List<FilmDto>? = null,
)