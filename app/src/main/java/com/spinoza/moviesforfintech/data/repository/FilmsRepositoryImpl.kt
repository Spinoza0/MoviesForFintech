package com.spinoza.moviesforfintech.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.spinoza.moviesforfintech.data.database.FilmsDao
import com.spinoza.moviesforfintech.data.mapper.FilmsMapper
import com.spinoza.moviesforfintech.data.network.ApiFactory
import com.spinoza.moviesforfintech.domain.model.Film
import com.spinoza.moviesforfintech.domain.model.FilmResponse
import com.spinoza.moviesforfintech.domain.repository.FilmsRepository
import com.spinoza.moviesforfintech.domain.repository.SourceType
import javax.inject.Inject

class FilmsRepositoryImpl @Inject constructor(
    private val mapper: FilmsMapper,
    private val filmsDao: FilmsDao,
) : FilmsRepository {

    private val apiService = ApiFactory.apiService
    private val allFilmsResponse = MutableLiveData<FilmResponse>()
    private val oneFilmResponse = MutableLiveData<FilmResponse>()
    private val isLoading = MutableLiveData(false)

    private var page = FIRST_PAGE
    private val allPopularFilms = mutableListOf<Film>()
    private lateinit var allFavouriteFilms: List<Film>
    private var sourceType = SourceType.WITHOUT_TYPE

    override fun getAllFilms(): LiveData<FilmResponse> = allFilmsResponse
    override fun getOneFilm(): LiveData<FilmResponse> = oneFilmResponse
    override fun getIsLoading(): LiveData<Boolean> = isLoading

    override suspend fun loadAllFilms() {
        if (sourceType == SourceType.WITHOUT_TYPE) {
            switchSourceTo(SourceType.POPULAR)
        } else if (isLoading.value == false) {
            if (sourceType == SourceType.POPULAR) {
                if (page <= MAX_PAGE) {
                    val newResponse = try {
                        isLoading.value = true
                        mapper.mapDtoToEntity(apiService.getTopPopularFilms(page))
                    } catch (e: Exception) {
                        FilmResponse(
                            e.localizedMessage ?: e.message ?: e.toString(),
                            listOf()
                        )
                    }
                    page++
                    val newFilms = mutableListOf<Film>()
                    allFilmsResponse.value?.let { newFilms.addAll(it.films) }
                    newFilms.addAll(newResponse.films)
                    allFilmsResponse.value =
                        newResponse.copy(error = newResponse.error, films = newFilms)
                    isLoading.value = false
                }
            } else {
                allFilmsResponse.value = FilmResponse("", allFavouriteFilms)
            }
        }
    }

    override suspend fun loadOneFilm(filmId: Int) {
        if (isLoading.value == false) {
            isLoading.value = true
            oneFilmResponse.value = if (sourceType == SourceType.POPULAR) {
                try {
                    val film = mapper.mapDtoToEntity(apiService.getFilmDescription(filmId))
                    FilmResponse("", listOf(film))
                } catch (e: Exception) {
                    FilmResponse(e.localizedMessage ?: e.message ?: e.toString(), listOf())
                }
            } else {
                val film = mapper.mapDbModelToEntity(filmsDao.getFavouriteFilm(filmId))
                FilmResponse("", listOf(film))
            }
            isLoading.value = false
        }
    }

    override suspend fun changeFavouriteStatus(film: Film) {
        val newFilm = film.copy(isFavourite = !film.isFavourite)
        if (newFilm.isFavourite) {
            filmsDao.insertFilmToFavourite(mapper.mapEntityToDbModel(newFilm))
        } else {
            filmsDao.removeFilmFromFavourite(newFilm.filmId)
        }

        if (sourceType == SourceType.FAVOURITE) {
            allFavouriteFilms = mapper.mapDbModelToEntity(filmsDao.getAllFavouriteFilms())
            allFilmsResponse.value = FilmResponse("", allFavouriteFilms)
        } else {
            allFilmsResponse.value?.let {
                val newFilms = it.films.map { oldFilm ->
                    if (oldFilm.filmId != newFilm.filmId) {
                        oldFilm
                    }
                    else {
                        newFilm
                    }
                }
                allFilmsResponse.value = FilmResponse("", newFilms)
            }
        }
    }

    override suspend fun switchSourceTo(target: SourceType) {
        if (target != sourceType && target != SourceType.WITHOUT_TYPE) {
            sourceType = target
            if (sourceType == SourceType.POPULAR) {

                if (allPopularFilms.size > 0) {
                    val newFilms = mutableListOf<Film>()
                    newFilms.addAll(allPopularFilms)
                    allFilmsResponse.value = FilmResponse("", newFilms)
                } else {
                    loadAllFilms()
                }
            } else {
                allFilmsResponse.value?.let {
                    allPopularFilms.addAll(it.films)
                }
                allFavouriteFilms = mapper.mapDbModelToEntity(filmsDao.getAllFavouriteFilms())
                allFilmsResponse.value = FilmResponse("", allFavouriteFilms)
            }
        }
    }

    companion object {
        private const val MAX_PAGE = 5
        private const val FIRST_PAGE = 1
    }
}