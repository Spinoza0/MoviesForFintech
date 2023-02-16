package com.spinoza.moviesforfintech.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.spinoza.moviesforfintech.data.database.FilmsDao
import com.spinoza.moviesforfintech.data.mapper.FilmsMapper
import com.spinoza.moviesforfintech.data.network.ApiService
import com.spinoza.moviesforfintech.data.network.model.FilmDescriptionDto
import com.spinoza.moviesforfintech.domain.model.Film
import com.spinoza.moviesforfintech.domain.model.FilmsState
import com.spinoza.moviesforfintech.domain.repository.FilmsRepository
import com.spinoza.moviesforfintech.domain.repository.ScreenType
import retrofit2.Response
import javax.inject.Inject

class FilmsRepositoryImpl @Inject constructor(
    private val mapper: FilmsMapper,
    private val filmsDao: FilmsDao,
    private val apiService: ApiService,
) : FilmsRepository {

    private val state = MutableLiveData<FilmsState>()
    private var page = FIRST_PAGE
    private val allPopularFilms = mutableListOf<Film>()
    private lateinit var allFavouriteFilms: List<Film>
    private var screenType = ScreenType.WITHOUT_TYPE

    override fun getState(): LiveData<FilmsState> = state

    override suspend fun loadAllFilms() {
        loadAllFilms(true)
    }

    private suspend fun loadAllFilms(copyOldData: Boolean) {
        if (screenType == ScreenType.WITHOUT_TYPE) {
            switchSourceTo(ScreenType.POPULAR)
        } else if (state.value != FilmsState.Loading) {
            if (screenType == ScreenType.POPULAR) {
                if (page <= MAX_PAGE) {
                    val response = apiService.getTopPopularFilms(page)
                    if (response.isSuccessful) {
                        page++

                        val newFilms = mutableListOf<Film>()
                        if (copyOldData) {
                            newFilms.addAll(allPopularFilms)
                        }

                        response.body()?.films?.let {
                            val films = mapper.mapDtoToEntity(it)
                            newFilms.addAll(films)
                            allPopularFilms.addAll(films)
                        }

                        state.value = FilmsState.AllFilms(newFilms)
                    } else {
                        state.value = getError(response)
                    }
                }
            } else {
                state.value = FilmsState.AllFilms(allFavouriteFilms)
            }
        }
    }

    private fun <T> getError(response: Response<T>): FilmsState.Error {
        val errorBody = response.errorBody()?.string() ?: ""
        return FilmsState.Error("${response.code()} $errorBody")
    }

    private fun getError(e: Throwable): FilmsState.Error =
        FilmsState.Error(e.localizedMessage ?: e.message ?: e.toString())

    override suspend fun loadOneFilm(filmId: Int) {
        if (state.value != FilmsState.Loading) {
            state.value = FilmsState.Loading
            if (screenType == ScreenType.POPULAR) {
                val response = apiService.getFilmDescription(filmId)
                if (response.isSuccessful) {
                    response.body()?.let {
                        state.value = FilmsState.OneFilm(mapper.mapDtoToEntity(it))
                    }
                } else {
                    tryGetFilmFromDatabase(filmId, response)
                }
            } else {
                getFilmFromDatabase(filmId)
            }
        }
    }

    private suspend fun getFilmFromFavourite(filmId: Int): Film =
        mapper.mapDbModelToEntity(filmsDao.getFavouriteFilm(filmId))

    private suspend fun getFilmFromDatabase(filmId: Int) {
        runCatching {
            state.value = FilmsState.OneFilm(getFilmFromFavourite(filmId))
        }.onFailure {
            state.value = getError(it)
        }
    }

    private suspend fun tryGetFilmFromDatabase(
        filmId: Int,
        response: Response<FilmDescriptionDto>,
    ) {
        runCatching {
            if (filmsDao.isFilmFavourite(filmId)) {
                state.value = FilmsState.OneFilm(getFilmFromFavourite(filmId))
            }
        }.onFailure {
            state.value = getError(response)
        }
    }

    override suspend fun changeFavouriteStatus(film: Film) {
        var newFilm = film.copy(isFavourite = !film.isFavourite)
        if (newFilm.isFavourite) {
            if (newFilm.description.isEmpty()) {
                val response = apiService.getFilmDescription(film.filmId)
                if (response.isSuccessful) {
                    response.body()?.let {
                        newFilm = mapper.mapDtoToEntity(it).copy(isFavourite = true)
                    }
                }
            }
        }

        runCatching {
            if (newFilm.isFavourite) {
                filmsDao.insertFilmToFavourite(mapper.mapEntityToDbModel(newFilm))
            } else {
                filmsDao.removeFilmFromFavourite(newFilm.filmId)
            }

            allPopularFilms.replaceAll {
                if (it.filmId == film.filmId) {
                    newFilm
                } else {
                    it
                }
            }

            if (screenType == ScreenType.FAVOURITE) {
                allFavouriteFilms = mapper.mapDbModelToEntity(filmsDao.getAllFavouriteFilms())
                state.value = FilmsState.AllFilms(allFavouriteFilms)
            } else {
                state.value = FilmsState.AllFilms(allPopularFilms.toList())
            }
        }.onFailure {
            state.value = getError(it)
        }
    }

    override suspend fun switchSourceTo(target: ScreenType) {
        if (target != screenType && target != ScreenType.WITHOUT_TYPE) {
            screenType = target
            if (screenType == ScreenType.POPULAR) {
                if (allPopularFilms.size > 0) {
                    state.value = FilmsState.AllFilms(allPopularFilms.toList())
                } else {
                    loadAllFilms(false)
                }
            } else {
                allFavouriteFilms = mapper.mapDbModelToEntity(filmsDao.getAllFavouriteFilms())
                state.value = FilmsState.AllFilms(allFavouriteFilms.toList())
            }
        }
    }

    companion object {
        private const val MAX_PAGE = 5
        private const val FIRST_PAGE = 1
    }
}