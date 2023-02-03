package com.spinoza.moviesforfintech.data.mapper

import com.spinoza.moviesforfintech.data.database.model.FilmDbModel
import com.spinoza.moviesforfintech.data.network.model.FilmDescriptionDto
import com.spinoza.moviesforfintech.data.network.model.FilmDto
import com.spinoza.moviesforfintech.data.network.model.ResponseDto
import com.spinoza.moviesforfintech.domain.model.Film
import com.spinoza.moviesforfintech.domain.model.FilmResponse

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

    fun mapDtoToEntity(filmDescriptionDto: FilmDescriptionDto): Film {
        val genres = mutableListOf<String>()
        filmDescriptionDto.genres?.forEach { genreDto ->
            genres.add(genreDto.genre ?: "")
        }

        val countries = mutableListOf<String>()
        filmDescriptionDto.countries?.forEach { countryDto ->
            countries.add(countryDto.country ?: "")
        }

        return Film(
            filmId = filmDescriptionDto.filmId,
            nameRu = filmDescriptionDto.nameRu,
            year = filmDescriptionDto.year,
            countries = countries,
            genres = genres,
            posterUrl = filmDescriptionDto.posterUrl,
            posterUrlPreview = filmDescriptionDto.posterUrlPreview,
            description = filmDescriptionDto.description
        )
    }

    private fun mapDtoToEntity(filmDto: FilmDto): Film {
        val genres = mutableListOf<String>()
        filmDto.genres?.forEach { genreDto ->
            genres.add(genreDto.genre ?: "")
        }

        val countries = mutableListOf<String>()
        filmDto.countries?.forEach { countryDto ->
            countries.add(countryDto.country ?: "")
        }

        return Film(
            filmId = filmDto.filmId,
            nameRu = filmDto.nameRu,
            year = filmDto.year,
            countries = countries,
            genres = genres,
            posterUrl = filmDto.posterUrl,
            posterUrlPreview = filmDto.posterUrlPreview,
            description = null
        )
    }

    fun mapDtoToEntity(responseDto: ResponseDto): FilmResponse {
        val films = mutableListOf<Film>()
        responseDto.films?.forEach { films.add(mapDtoToEntity(it)) }
        return FilmResponse(responseDto.pagesCount, films)
    }
}