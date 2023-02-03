package com.spinoza.moviesforfintech.data.mapper

import com.spinoza.moviesforfintech.data.database.model.CountryDbModel
import com.spinoza.moviesforfintech.data.database.model.FilmDbModel
import com.spinoza.moviesforfintech.data.database.model.GenreDbModel
import com.spinoza.moviesforfintech.domain.model.Country
import com.spinoza.moviesforfintech.domain.model.Film
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
}