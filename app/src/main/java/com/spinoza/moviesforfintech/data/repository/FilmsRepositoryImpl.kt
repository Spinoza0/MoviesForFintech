package com.spinoza.moviesforfintech.data.repository

import androidx.lifecycle.Transformations
import com.spinoza.moviesforfintech.data.database.FilmsDao
import com.spinoza.moviesforfintech.data.mapper.FilmsMapper
import com.spinoza.moviesforfintech.data.network.ApiFactory
import com.spinoza.moviesforfintech.domain.model.Film
import com.spinoza.moviesforfintech.domain.repository.FilmsRepository
import javax.inject.Inject

class FilmsRepositoryImpl @Inject constructor(
    private val mapper: FilmsMapper,
    private val filmsDao: FilmsDao,
) : FilmsRepository {

    private val apiService = ApiFactory.apiService

    override suspend fun loadFilmsFromNetwork(page: Int) =
        mapper.mapDtoToEntity(apiService.getTopPopularFilms(page))

    override suspend fun loadFilmFromNetwork(filmId: Int) =
        mapper.mapDtoToEntity(apiService.getFilmDescription(filmId))

    override suspend fun getAllFavouriteFilms() =
        Transformations.map(filmsDao.getAllFavouriteFilms()) { list ->
            list.map { mapper.mapDbModelToEntity(it) }
        }

    override suspend fun getFavouriteFilm(filmId: Int) =
        Transformations.map(filmsDao.getFavouriteFilm(filmId)) {
            mapper.mapDbModelToEntity(it)
        }

    override suspend fun insertFilmToFavourite(film: Film) =
        filmsDao.insertFilmToFavourite(mapper.mapEntityToDbModel(film))

    override suspend fun removeFilmFromFavourite(filmId: Int) {
        filmsDao.removeFilmFromFavourite(filmId)
    }
}