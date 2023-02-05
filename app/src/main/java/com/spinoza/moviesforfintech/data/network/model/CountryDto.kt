package com.spinoza.moviesforfintech.data.network.model

import com.google.gson.annotations.SerializedName

data class CountryDto(
    @SerializedName("country")
    val country: String? = null,
)