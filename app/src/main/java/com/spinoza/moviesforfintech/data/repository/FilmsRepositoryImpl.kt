package com.spinoza.moviesforfintech.data.repository

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

    override suspend fun loadFilmsFromNetwork(page: Int) = try {
        mapper.mapDtoToEntity(apiService.getTopPopularFilms(page))
    } catch (e: Exception) {
        FilmResponse(0, e.localizedMessage ?: e.message ?: e.toString(), listOf())
    }

    override suspend fun loadFilmFromNetwork(filmId: Int) = try {
        val film = mapper.mapDtoToEntity(apiService.getFilmDescription(filmId))
        FilmResponse(1, "", listOf(film))
    } catch (e: Exception) {
        FilmResponse(0, e.localizedMessage ?: e.message ?: e.toString(), listOf())
    }

    override suspend fun getAllFavouriteFilms(): List<Film> =
        filmsDao.getAllFavouriteFilms().map { mapper.mapDbModelToEntity(it) }


    override suspend fun getFavouriteFilm(filmId: Int): Film =
        mapper.mapDbModelToEntity(filmsDao.getFavouriteFilm(filmId))

    override suspend fun insertFilmToFavourite(film: Film) =
        filmsDao.insertFilmToFavourite(mapper.mapEntityToDbModel(film))

    override suspend fun removeFilmFromFavourite(filmId: Int) {
        filmsDao.removeFilmFromFavourite(filmId)
    }
}