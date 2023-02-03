package com.spinoza.moviesforfintech.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.spinoza.moviesforfintech.data.database.DbConstants.Companion.FAVOURITE_TABLE

@Entity(tableName = FAVOURITE_TABLE)
data class FilmDbModel(
    @PrimaryKey
    val filmId: Int?,
    val nameRu: String? = null,
    val year: Int? = null,
    val countries: List<CountryDbModel>,
    val genres: List<GenreDbModel>,
    val posterUrl: String? = null,
    val description: String? = null,
)