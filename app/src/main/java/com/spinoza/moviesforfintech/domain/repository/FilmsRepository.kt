package com.spinoza.moviesforfintech.domain.repository

import androidx.lifecycle.LiveData
import com.spinoza.moviesforfintech.domain.model.Film
import com.spinoza.moviesforfintech.domain.model.FilmResponse

interface FilmsRepository {
    fun getFilmsFromNetwork(): LiveData<FilmResponse>
    fun getOneFilmFromNetwork(): LiveData<FilmResponse>
    suspend fun loadFilmsFromNetwork()
    suspend fun loadOneFilmFromNetwork(filmId: Int)
    fun getIsLoading(): LiveData<Boolean>

    suspend fun getAllFavouriteFilms(): List<Film>
    suspend fun getFavouriteFilm(filmId: Int): Film
    suspend fun changeFavouriteStatus(film: Film)
}