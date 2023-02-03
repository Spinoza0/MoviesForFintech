package com.spinoza.moviesforfintech.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.spinoza.moviesforfintech.data.database.DbConstants.Companion.FAVOURITE_TABLE
import com.spinoza.moviesforfintech.domain.model.Film

@Dao
interface FilmsDao {

    @Query("SELECT * FROM $FAVOURITE_TABLE")
    fun getAllFavouriteFilms(): LiveData<List<Film>>

    @Query("SELECT * FROM $FAVOURITE_TABLE WHERE filmId=:filmId")
    fun getFavouriteFilm(filmId: Int): LiveData<Film>

    @Insert
    fun insertFilmToFavourite(film: Film)

    @Query("DELETE FROM $FAVOURITE_TABLE WHERE filmId=:filmId")
    fun removeFilmFromFavourite(filmId: Int)
}