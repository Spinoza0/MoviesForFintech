package com.spinoza.moviesforfintech.data.mapper

import com.spinoza.moviesforfintech.data.database.model.CountryDbModel
import com.spinoza.moviesforfintech.data.database.model.FilmDbModel
import com.spinoza.moviesforfintech.data.database.model.GenreDbModel
import com.spinoza.moviesforfintech.data.network.model.FilmDto
import com.spinoza.moviesforfintech.domain.model.Country
import com.spinoza.moviesforfintech.domain.model.Film
import com.spinoza.moviesforfintech.domain.model.Genre

class FilmMapper {
    fun mapDtoToDbModel(filmDto: FilmDto): FilmDbModel {
        val genresDbModel = mutableListOf<GenreDbModel>()
        filmDto.genres?.let {
            it.forEach { genreDto -> genresDbModel.add(GenreDbModel(genreDto.genre)) }
        }
        val countriesDbModel = mutableListOf<CountryDbModel>()
        filmDto.countries?.let {
            it.forEach { countryDto -> countriesDbModel.add(CountryDbModel(countryDto.country)) }
        }
        return FilmDbModel(
            filmId = filmDto.filmId,
            nameRu = filmDto.nameRu,
            year = filmDto.year,
            countries = countriesDbModel,
            genres = genresDbModel,
            posterUrl = filmDto.posterUrl
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
            posterUrl = filmDbModel.posterUrl
        )
    }
}