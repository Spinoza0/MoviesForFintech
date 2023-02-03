package com.spinoza.moviesforfintech.domain.repository

import androidx.lifecycle.LiveData
import com.spinoza.moviesforfintech.domain.model.Film
import com.spinoza.moviesforfintech.domain.model.FilmResponse

interface FilmsRepository {
    suspend fun loadFilmsFromNetwork(page: Int): FilmResponse
    suspend fun loadFilmFromNetwork(filmId: Int): Film

    suspend fun getAllFavouriteFilms(): LiveData<List<Film>>
    suspend fun getFavouriteFilm(filmId: Int): LiveData<Film>
    suspend fun insertFilmToFavourite(film: Film)
    suspend fun removeFilmFromFavourite(filmId: Int)
}