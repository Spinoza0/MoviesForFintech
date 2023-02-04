package com.spinoza.moviesforfintech.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.spinoza.moviesforfintech.data.database.DbConstants.Companion.FAVOURITE_TABLE
import com.spinoza.moviesforfintech.data.database.model.FilmDbModel

@Dao
interface FilmsDao {

    @Query("SELECT * FROM $FAVOURITE_TABLE")
    suspend fun getAllFavouriteFilms(): List<FilmDbModel>

    @Query("SELECT * FROM $FAVOURITE_TABLE WHERE filmId=:filmId")
    suspend fun getFavouriteFilm(filmId: Int): FilmDbModel

    @Query("SELECT EXISTS (SELECT * FROM $FAVOURITE_TABLE WHERE filmId=:filmId)")
    suspend fun isFilmFavourite(filmId: Int): Boolean

    @Insert
    suspend fun insertFilmToFavourite(film: FilmDbModel)

    @Query("DELETE FROM $FAVOURITE_TABLE WHERE filmId=:filmId")
    suspend fun removeFilmFromFavourite(filmId: Int)
}