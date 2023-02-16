package com.spinoza.moviesforfintech.data.mapper

import com.spinoza.moviesforfintech.data.database.DbConstants
import com.spinoza.moviesforfintech.data.database.FilmsDao
import com.spinoza.moviesforfintech.data.database.model.FilmDbModel
import com.spinoza.moviesforfintech.data.network.model.CountryDto
import com.spinoza.moviesforfintech.data.network.model.FilmDescriptionDto
import com.spinoza.moviesforfintech.data.network.model.FilmDto
import com.spinoza.moviesforfintech.data.network.model.GenreDto
import com.spinoza.moviesforfintech.domain.model.Film
import java.util.stream.Collectors
import javax.inject.Inject

class FilmsMapper @Inject constructor(private val filmsDao: FilmsDao) {
    fun mapEntityToDbModel(film: Film) = FilmDbModel(
        filmId = film.filmId,
        nameRu = film.nameRu,
        year = film.year,
        countries = film.countries,
        genres = film.genres,
        posterUrl = film.posterUrl,
        posterUrlPreview = film.posterUrlPreview,
        description = film.description
    )

    fun mapDbModelToEntity(filmDbModel: FilmDbModel) = Film(
        filmId = filmDbModel.filmId,
        nameRu = filmDbModel.nameRu,
        year = filmDbModel.year,
        countries = filmDbModel.countries,
        genres = filmDbModel.genres,
        posterUrl = filmDbModel.posterUrl,
        posterUrlPreview = filmDbModel.posterUrlPreview,
        description = filmDbModel.description,
        isFavourite = true
    )

    fun mapDbModelToEntity(filmDbModelList: List<FilmDbModel>) =
        filmDbModelList.map { mapDbModelToEntity(it) }

    suspend fun mapDtoToEntity(filmDescriptionDto: FilmDescriptionDto): Film {
        val filmId = filmDescriptionDto.filmId ?: 0
        val isFavourite = when (filmDescriptionDto.filmId) {
            null -> false
            else -> filmsDao.isFilmFavourite(filmId)
        }
        return Film(
            filmId = filmId,
            nameRu = filmDescriptionDto.nameRu ?: "",
            year = filmDescriptionDto.year ?: 0,
            countries = countriesDtoToString(filmDescriptionDto.countries),
            genres = genresDtoToString(filmDescriptionDto.genres),
            posterUrl = filmDescriptionDto.posterUrl ?: "",
            posterUrlPreview = filmDescriptionDto.posterUrlPreview ?: "",
            description = filmDescriptionDto.description ?: "",
            isFavourite = isFavourite
        )
    }

    private suspend fun mapDtoToEntity(filmDto: FilmDto): Film {
        val filmId = filmDto.filmId ?: 0
        val isFavourite = when (filmDto.filmId) {
            null -> false
            else -> filmsDao.isFilmFavourite(filmId)
        }
        return Film(
            filmId = filmId,
            nameRu = filmDto.nameRu ?: "",
            year = filmDto.year ?: 0,
            countries = countriesDtoToString(filmDto.countries),
            genres = genresDtoToString(filmDto.genres),
            posterUrl = filmDto.posterUrl ?: "",
            posterUrlPreview = filmDto.posterUrlPreview ?: "",
            description = "",
            isFavourite = isFavourite
        )
    }

    suspend fun mapDtoToEntity(filmsDto: List<FilmDto>): List<Film> =
        filmsDto.map { mapDtoToEntity(it) }

    private fun genresDtoToString(genresDto: List<GenreDto>?): String {
        val genres = mutableListOf<String>()
        genresDto?.forEach { genres.add(it.genre ?: "") }
        return listToString(genres)
    }

    private fun countriesDtoToString(countriesDto: List<CountryDto>?): String {
        val countries = mutableListOf<String>()
        countriesDto?.forEach { countries.add(it.country ?: "") }
        return listToString(countries)
    }

    private fun listToString(list: List<String>): String {
        return list.stream().collect(Collectors.joining(DbConstants.DELIMITER))
    }
}