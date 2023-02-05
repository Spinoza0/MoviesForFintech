package com.spinoza.moviesforfintech.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spinoza.moviesforfintech.domain.model.Film
import com.spinoza.moviesforfintech.domain.repository.FilmsRepository
import com.spinoza.moviesforfintech.domain.repository.ScreenType
import kotlinx.coroutines.launch
import javax.inject.Inject

class PopularFilmsViewModel @Inject constructor(private val repository: FilmsRepository) :
    ViewModel() {

    val allFilmsResponse = repository.getAllFilms()
    val oneFilmResponse = repository.getOneFilm()
    val isLoading = repository.getIsLoading()

    private val _screenType = MutableLiveData<ScreenType>()
    val screenType: LiveData<ScreenType>
        get() = _screenType

    init {
        _screenType.value = ScreenType.POPULAR
    }

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

    fun switchSourceTo(target: ScreenType) {
        viewModelScope.launch {
            repository.switchSourceTo(target)
        }
        _screenType.value = target
    }
}