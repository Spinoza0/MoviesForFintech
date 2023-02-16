package com.spinoza.moviesforfintech.domain.repository

import androidx.lifecycle.LiveData
import com.spinoza.moviesforfintech.domain.model.Film
import com.spinoza.moviesforfintech.domain.model.FilmsState

interface FilmsRepository {
    fun getState(): LiveData<FilmsState>
    suspend fun loadAllFilms()
    suspend fun loadOneFilm(filmId: Int)
    suspend fun changeFavouriteStatus(film: Film)
    suspend fun switchSourceTo(target: ScreenType)
}