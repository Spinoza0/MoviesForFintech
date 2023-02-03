package com.spinoza.moviesforfintech.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "films")
data class FilmDbModel(
    @PrimaryKey
    val filmId: Int?,
    val nameRu: String? = null,
    val year: String? = null,
    val countries: List<CountryDbModel>,
    val genres: List<GenreDbModel>,
    val posterUrl: String? = null,
)