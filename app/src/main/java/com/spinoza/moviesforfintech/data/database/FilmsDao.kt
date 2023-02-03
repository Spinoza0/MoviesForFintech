package com.spinoza.moviesforfintech.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.spinoza.moviesforfintech.data.database.DbConstants.Companion.FAVOURITE_TABLE
import com.spinoza.moviesforfintech.data.database.model.FilmDbModel

@Dao
interface FilmsDao {

    @Query("SELECT * FROM $FAVOURITE_TABLE")
    fun getAllFavouriteFilms(): LiveData<List<FilmDbModel>>

    @Query("SELECT * FROM $FAVOURITE_TABLE WHERE filmId=:filmId")
    fun getFavouriteFilm(filmId: Int): LiveData<FilmDbModel>

    @Insert
    fun insertFilmToFavourite(film: FilmDbModel)

    @Query("DELETE FROM $FAVOURITE_TABLE WHERE filmId=:filmId")
    fun removeFilmFromFavourite(filmId: Int)
}