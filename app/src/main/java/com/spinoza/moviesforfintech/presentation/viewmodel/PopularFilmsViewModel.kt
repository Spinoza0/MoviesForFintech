package com.spinoza.moviesforfintech.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spinoza.moviesforfintech.domain.model.Film
import com.spinoza.moviesforfintech.domain.repository.FilmsRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class PopularFilmsViewModel @Inject constructor(private val repository: FilmsRepository) :
    ViewModel() {

    val filmResponse = repository.getFilmsFromNetwork()
    val isLoading = repository.getIsLoading()

    init {
        loadMovies()
    }

    fun loadMovies() {
        viewModelScope.launch {
            repository.loadFilmsFromNetwork()
        }
    }

    fun changeFavouriteStatus(film: Film) {
        viewModelScope.launch {
            repository.changeFavouriteStatus(film)
        }
    }
}