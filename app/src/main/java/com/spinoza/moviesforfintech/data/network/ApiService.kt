package com.spinoza.moviesforfintech.data.network

import com.spinoza.moviesforfintech.data.network.model.FilmDto
import com.spinoza.moviesforfintech.data.network.model.ResponseDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("top?type=TOP_100_POPULAR_FILMS")
    suspend fun getTopPopularFilms(
        @Header("X-API-KEY: $API_KEY")
        @Query(QUERY_PARAM_PAGE) page: Int,
    ): ResponseDto


    @GET("top/{$QUERY_PATH_ID}")
    suspend fun getFilmDescription(
        @Header("X-API-KEY: $API_KEY")
        @Path(QUERY_PATH_ID) id: String,
    ): FilmDto

    companion object {
        private const val QUERY_PARAM_PAGE = "page"
        private const val QUERY_PATH_ID = "id"

        private const val API_KEY = "d985726c-2563-4b5a-bed8-0857d1b8944c"
        // key from tinkoff
        // private const val API_KEY = "e30ffed0-76ab-4dd6-b41f-4c9da2b2735b"
    }
}