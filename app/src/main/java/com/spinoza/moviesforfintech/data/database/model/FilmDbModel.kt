package com.spinoza.moviesforfintech.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.spinoza.moviesforfintech.data.database.DbConstants.Companion.FAVOURITE_TABLE

@Entity(tableName = FAVOURITE_TABLE)
data class FilmDbModel(
    @PrimaryKey
    val filmId: Int,
    val nameRu: String,
    val year: Int,
    val countries: String,
    val genres: String,
    val posterUrl: String,
    val posterUrlPreview: String,
    val description: String,
)