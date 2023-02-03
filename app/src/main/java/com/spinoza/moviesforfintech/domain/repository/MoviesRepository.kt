package com.spinoza.moviesforfintech.domain.repository

import com.spinoza.moviesforfintech.domain.model.Film
import com.spinoza.moviesforfintech.domain.model.FilmResponse

interface MoviesRepository {
    fun loadMoviesFromNetwork(page: Int): FilmResponse
    fun loadFilmFromNetwork(filmId: Int): Film
    fun getAllFavouriteMovies(): List<Film>
    fun getFavouriteMovie(filmId: Int): Film
    fun insertMovieToFavourite(film: Film)
    fun removeMovieFromFavourite(film: Film)
}