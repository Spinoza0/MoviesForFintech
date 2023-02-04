package com.spinoza.moviesforfintech.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spinoza.moviesforfintech.domain.model.Film
import com.spinoza.moviesforfintech.domain.repository.FilmsRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class PopularFilmsViewModel @Inject constructor(private val repository: FilmsRepository) :
    ViewModel() {

    val filmsResponse = repository.getFilmsFromNetwork()
    val oneFilmResponse = repository.getOneFilmFromNetwork()
    val isLoading = repository.getIsLoading()

    init {
        loadFilms()
    }

    fun loadFilms() {
        viewModelScope.launch {
            repository.loadFilmsFromNetwork()
        }
    }

    fun loadFullFilmData(filmId: Int) {
        viewModelScope.launch {
            repository.loadOneFilmFromNetwork(filmId)
        }
    }

    fun changeFavouriteStatus(film: Film) {
        viewModelScope.launch {
            repository.changeFavouriteStatus(film)
        }
    }
}