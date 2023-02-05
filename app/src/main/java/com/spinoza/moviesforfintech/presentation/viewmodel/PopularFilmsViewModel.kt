package com.spinoza.moviesforfintech.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spinoza.moviesforfintech.domain.model.Film
import com.spinoza.moviesforfintech.domain.repository.FilmsRepository
import com.spinoza.moviesforfintech.domain.repository.SourceType
import kotlinx.coroutines.launch
import javax.inject.Inject

class PopularFilmsViewModel @Inject constructor(private val repository: FilmsRepository) :
    ViewModel() {

    val allFilmsResponse = repository.getAllFilms()
    val oneFilmResponse = repository.getOneFilm()
    val isLoading = repository.getIsLoading()

    fun loadAllFilms() {
        viewModelScope.launch {
            repository.loadAllFilms()
        }
    }

    fun loadFullFilmData(filmId: Int) {
        viewModelScope.launch {
            repository.loadOneFilm(filmId)
        }
    }

    fun changeFavouriteStatus(film: Film) {
        viewModelScope.launch {
            repository.changeFavouriteStatus(film)
        }
    }

    fun switchSourceTo(target: SourceType) {
        viewModelScope.launch {
            repository.switchSourceTo(target)
        }
    }
}