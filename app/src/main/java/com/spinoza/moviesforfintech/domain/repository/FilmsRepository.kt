package com.spinoza.moviesforfintech.domain.repository

import com.spinoza.moviesforfintech.domain.model.Film
import com.spinoza.moviesforfintech.domain.model.FilmResponse

interface FilmsRepository {
    suspend fun loadFilmsFromNetwork(page: Int): FilmResponse
    suspend fun loadFilmFromNetwork(filmId: Int): FilmResponse

    suspend fun getAllFavouriteFilms(): List<Film>
    suspend fun getFavouriteFilm(filmId: Int): Film
    suspend fun insertFilmToFavourite(film: Film)
    suspend fun removeFilmFromFavourite(filmId: Int)
}