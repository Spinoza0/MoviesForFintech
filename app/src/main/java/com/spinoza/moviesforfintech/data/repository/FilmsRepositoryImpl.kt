package com.spinoza.moviesforfintech.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.spinoza.moviesforfintech.data.database.FilmsDao
import com.spinoza.moviesforfintech.data.mapper.FilmsMapper
import com.spinoza.moviesforfintech.data.network.ApiFactory
import com.spinoza.moviesforfintech.domain.model.Film
import com.spinoza.moviesforfintech.domain.model.FilmResponse
import com.spinoza.moviesforfintech.domain.repository.FilmsRepository
import javax.inject.Inject

class FilmsRepositoryImpl @Inject constructor(
    private val mapper: FilmsMapper,
    private val filmsDao: FilmsDao,
) : FilmsRepository {

    private val apiService = ApiFactory.apiService
    private val filmsResponse = MutableLiveData<FilmResponse>()
    private val oneFilmResponse = MutableLiveData<FilmResponse>()
    private val isLoading = MutableLiveData<Boolean>(false)

    private var page = FIRST_PAGE

    override fun getFilmsFromNetwork(): LiveData<FilmResponse> = filmsResponse
    override fun getOneFilmFromNetwork(): LiveData<FilmResponse> = oneFilmResponse
    override fun getIsLoading(): LiveData<Boolean> = isLoading

    override suspend fun loadFilmsFromNetwork() {
        isLoading.value?.let { loading ->
            if (page <= MAX_PAGE && !loading) {
                val newResponse = try {
                    isLoading.value = true
                    mapper.mapDtoToEntity(apiService.getTopPopularFilms(page))
                } catch (e: Exception) {
                    FilmResponse(e.localizedMessage ?: e.message ?: e.toString(), listOf())
                }
                page++
                val newFilms = mutableListOf<Film>()
                filmsResponse.value?.let { newFilms.addAll(it.films) }
                newFilms.addAll(newResponse.films)
                filmsResponse.value = newResponse.copy(error = newResponse.error, films = newFilms)
                isLoading.value = false
            }
        }
    }

    override suspend fun loadOneFilmFromNetwork(filmId: Int) {
        isLoading.value?.let { loading ->
            if (!loading) {
                isLoading.value = true
                oneFilmResponse.value = try {
                    val film = mapper.mapDtoToEntity(apiService.getFilmDescription(filmId))
                    FilmResponse("", listOf(film))
                } catch (e: Exception) {
                    FilmResponse(e.localizedMessage ?: e.message ?: e.toString(), listOf())
                }
                isLoading.value = false
            }
        }
    }

    override suspend fun getAllFavouriteFilms(): List<Film> =
        filmsDao.getAllFavouriteFilms().map { mapper.mapDbModelToEntity(it) }

    override suspend fun getFavouriteFilm(filmId: Int): Film =
        mapper.mapDbModelToEntity(filmsDao.getFavouriteFilm(filmId))

    override suspend fun changeFavouriteStatus(film: Film) {
        val newFilm = film.copy(isFavourite = !film.isFavourite)
        if (newFilm.isFavourite) {
            filmsDao.insertFilmToFavourite(mapper.mapEntityToDbModel(newFilm))
        } else {
            filmsDao.removeFilmFromFavourite(newFilm.filmId)
        }
        val newFilms = mutableListOf<Film>()
        filmsResponse.value?.let {
            it.films.forEach { oldFilm ->
                if (oldFilm.filmId == newFilm.filmId) {
                    newFilms.add(newFilm)
                } else {
                    newFilms.add(oldFilm)
                }
            }
        }
        filmsResponse.value = FilmResponse("", newFilms)
    }

    companion object {
        private const val MAX_PAGE = 5
        private const val FIRST_PAGE = 1
    }
}