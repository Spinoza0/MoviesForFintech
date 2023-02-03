package com.spinoza.moviesforfintech.data.mapper

import com.spinoza.moviesforfintech.data.database.model.CountryDbModel
import com.spinoza.moviesforfintech.data.database.model.FilmDbModel
import com.spinoza.moviesforfintech.data.database.model.GenreDbModel
import com.spinoza.moviesforfintech.data.network.model.FilmDescriptionDto
import com.spinoza.moviesforfintech.data.network.model.FilmDto
import com.spinoza.moviesforfintech.data.network.model.ResponseDto
import com.spinoza.moviesforfintech.domain.model.Country
import com.spinoza.moviesforfintech.domain.model.Film
import com.spinoza.moviesforfintech.domain.model.FilmResponse
import com.spinoza.moviesforfintech.domain.model.Genre

class FilmsMapper {
    fun mapEntityToDbModel(film: Film): FilmDbModel {
        val genresDbModel = mutableListOf<GenreDbModel>()
        film.genres.forEach { genreDto -> genresDbModel.add(GenreDbModel(genreDto.genre)) }

        val countriesDbModel = mutableListOf<CountryDbModel>()
        film.countries.forEach { countryDto -> countriesDbModel.add(CountryDbModel(countryDto.country)) }

        return FilmDbModel(
            filmId = film.filmId,
            nameRu = film.nameRu,
            year = film.year,
            countries = countriesDbModel,
            genres = genresDbModel,
            posterUrl = film.posterUrl,
            description = film.description
        )
    }

    fun mapDbModelToEntity(filmDbModel: FilmDbModel): Film {
        val genres = mutableListOf<Genre>()
        filmDbModel.genres.forEach { genreDbModel -> genres.add(Genre(genreDbModel.genre)) }

        val countries = mutableListOf<Country>()
        filmDbModel.countries.forEach { countryDbModel ->
            countries.add(Country(countryDbModel.country))
        }

        return Film(
            filmId = filmDbModel.filmId,
            nameRu = filmDbModel.nameRu,
            year = filmDbModel.year,
            countries = countries,
            genres = genres,
            posterUrl = filmDbModel.posterUrl,
            description = filmDbModel.description
        )
    }

    fun mapDtoToEntity(filmDescriptionDto: FilmDescriptionDto): Film {
        val genres = mutableListOf<Genre>()
        filmDescriptionDto.genres?.forEach { genreDto -> genres.add(Genre(genreDto.genre)) }

        val countries = mutableListOf<Country>()
        filmDescriptionDto.countries?.forEach { countryDto ->
            countries.add(Country(countryDto.country))
        }

        return Film(
            filmId = filmDescriptionDto.filmId,
            nameRu = filmDescriptionDto.nameRu,
            year = filmDescriptionDto.year,
            countries = countries,
            genres = genres,
            posterUrl = filmDescriptionDto.posterUrl,
            description = filmDescriptionDto.description
        )
    }

    fun mapDtoToEntity(filmDto: FilmDto): Film {
        val genres = mutableListOf<Genre>()
        filmDto.genres?.forEach { genreDto -> genres.add(Genre(genreDto.genre)) }

        val countries = mutableListOf<Country>()
        filmDto.countries?.forEach { countryDto ->
            countries.add(Country(countryDto.country))
        }

        return Film(
            filmId = filmDto.filmId,
            nameRu = filmDto.nameRu,
            year = filmDto.year,
            countries = countries,
            genres = genres,
            posterUrl = filmDto.posterUrl,
            description = null
        )
    }

    fun mapDtoToEntity(responseDto: ResponseDto): FilmResponse {
        val films = mutableListOf<Film>()
        responseDto.films?.forEach { films.add(mapDtoToEntity(it)) }
        return FilmResponse(responseDto.pagesCount, films)
    }
}