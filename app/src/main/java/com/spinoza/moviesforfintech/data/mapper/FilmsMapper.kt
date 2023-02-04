package com.spinoza.moviesforfintech.data.mapper

import com.spinoza.moviesforfintech.data.database.DbConstants
import com.spinoza.moviesforfintech.data.database.model.FilmDbModel
import com.spinoza.moviesforfintech.data.network.model.*
import com.spinoza.moviesforfintech.domain.model.Film
import com.spinoza.moviesforfintech.domain.model.FilmResponse
import java.util.stream.Collectors

class FilmsMapper {
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
        description = filmDbModel.description
    )

    fun mapDtoToEntity(filmDescriptionDto: FilmDescriptionDto) = Film(
        filmId = filmDescriptionDto.filmId ?: 0,
        nameRu = filmDescriptionDto.nameRu ?: "",
        year = filmDescriptionDto.year ?: 0,
        countries = countriesDtoToString(filmDescriptionDto.countries),
        genres = genresDtoToString(filmDescriptionDto.genres),
        posterUrl = filmDescriptionDto.posterUrl ?: "",
        posterUrlPreview = filmDescriptionDto.posterUrlPreview ?: "",
        description = filmDescriptionDto.description ?: ""
    )

    private fun mapDtoToEntity(filmDto: FilmDto) = Film(
        filmId = filmDto.filmId ?: 0,
        nameRu = filmDto.nameRu ?: "",
        year = filmDto.year ?: 0,
        countries = countriesDtoToString(filmDto.countries),
        genres = genresDtoToString(filmDto.genres),
        posterUrl = filmDto.posterUrl ?: "",
        posterUrlPreview = filmDto.posterUrlPreview ?: "",
        description = ""
    )

    fun mapDtoToEntity(responseDto: ResponseDto): FilmResponse {
        val films = mutableListOf<Film>()
        responseDto.films?.forEach { films.add(mapDtoToEntity(it)) }
        return FilmResponse(responseDto.pagesCount, films)
    }

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