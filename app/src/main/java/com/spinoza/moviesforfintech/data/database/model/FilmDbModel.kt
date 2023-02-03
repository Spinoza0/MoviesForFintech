package com.spinoza.moviesforfintech.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.spinoza.moviesforfintech.data.database.DbConstants.Companion.FAVOURITE_TABLE
import com.spinoza.moviesforfintech.data.mapper.Converters

@Entity(tableName = FAVOURITE_TABLE)
@TypeConverters(Converters::class)
data class FilmDbModel(
    @PrimaryKey
    val filmId: Int?,
    val nameRu: String? = null,
    val year: Int? = null,
    val countries: List<String>,
    val genres: List<String>,
    val posterUrl: String? = null,
    val posterUrlPreview: String? = null,
    val description: String? = null,
)