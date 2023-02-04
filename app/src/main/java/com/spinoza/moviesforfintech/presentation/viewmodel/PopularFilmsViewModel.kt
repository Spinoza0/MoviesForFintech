package com.spinoza.moviesforfintech.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spinoza.moviesforfintech.domain.model.Film
import com.spinoza.moviesforfintech.domain.repository.FilmsRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class PopularFilmsViewModel @Inject constructor(private val repository: FilmsRepository) :
    ViewModel() {

    private val _films = MutableLiveData<List<Film>>()
    private val _isLoading = MutableLiveData(false)
    private val _isError: MutableLiveData<String> = MutableLiveData()
    private var page = 1
    private var maxPage: Int? = null

    val films: LiveData<List<Film>>
        get() = _films
    val isLoading: LiveData<Boolean>
        get() = _isLoading
    val isError: LiveData<String>
        get() = _isError

    init {
        loadMovies()
    }

    fun loadMovies() {
        maxPage?.let {
            if (page > it)
                return
        }

        isLoading.value?.let {
            viewModelScope.launch {
                _isLoading.value = true
                val response = repository.loadFilmsFromNetwork(page)
                _isLoading.value = false
                if (response.error.isEmpty()) {
                    val loadedFilms = mutableListOf<Film>()
                    _films.value?.let {
                        loadedFilms.addAll(it)
                    }
                    loadedFilms.addAll(response.films)
                    _films.value = loadedFilms

                    if (maxPage == null) {
                        maxPage = response.pagesCount
                    }
                    page++
                } else {
                    _isError.value = response.error
                }
            }
        }
    }
}