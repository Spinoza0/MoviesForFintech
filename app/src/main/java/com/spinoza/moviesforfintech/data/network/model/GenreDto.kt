package com.spinoza.moviesforfintech.data.network.model

import com.google.gson.annotations.SerializedName

data class GenreDto(
    @SerializedName("genre")
    var genre: String? = null,
)