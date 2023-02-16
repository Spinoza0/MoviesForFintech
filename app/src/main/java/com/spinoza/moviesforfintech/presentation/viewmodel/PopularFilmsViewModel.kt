package com.spinoza.moviesforfintech.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spinoza.moviesforfintech.domain.model.Film
import com.spinoza.moviesforfintech.domain.repository.ScreenType
import com.spinoza.moviesforfintech.domain.usecase.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class PopularFilmsViewModel @Inject constructor(
    getStateUseCase: GetStateUseCase,
    private var loadAllFilmsUseCase: LoadAllFilmsUseCase,
    private var loadOneFilmUseCase: LoadOneFilmUseCase,
    private var changeFavouriteStatusUseCase: ChangeFavouriteStatusUseCase,
    private var switchSourceToUseCase: SwitchSourceToUseCase,
) : ViewModel() {

    val state = getStateUseCase()

    private val _screenType = MutableLiveData<ScreenType>()
    val screenType: LiveData<ScreenType>
        get() = _screenType

    init {
        _screenType.value = ScreenType.POPULAR
    }

    fun loadAllFilms() {
        viewModelScope.launch {
            loadAllFilmsUseCase()
        }
    }

    fun loadFullFilmData(filmId: Int) {
        viewModelScope.launch {
            loadOneFilmUseCase(filmId)
        }
    }

    fun changeFavouriteStatus(film: Film) {
        viewModelScope.launch {
            changeFavouriteStatusUseCase(film)
        }
    }

    fun switchSourceTo(target: ScreenType) {
        viewModelScope.launch {
            switchSourceToUseCase(target)
        }
        _screenType.value = target
    }
}