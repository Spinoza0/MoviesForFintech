package com.spinoza.moviesforfintech.domain.repository

import androidx.lifecycle.LiveData
import com.spinoza.moviesforfintech.domain.model.Film
import com.spinoza.moviesforfintech.domain.model.FilmResponse

interface FilmsRepository {
    fun getAllFilms(): LiveData<FilmResponse>
    fun getOneFilm(): LiveData<FilmResponse>
    suspend fun loadAllFilms()
    suspend fun loadOneFilm(filmId: Int)
    fun getIsLoading(): LiveData<Boolean>
    suspend fun changeFavouriteStatus(film: Film)
    suspend fun switchSourceTo(target: ScreenType)
}