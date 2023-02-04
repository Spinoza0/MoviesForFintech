package com.spinoza.moviesforfintech.data.network

import com.spinoza.moviesforfintech.data.network.model.FilmDescriptionDto
import com.spinoza.moviesforfintech.data.network.model.ResponseDto
import retrofit2.http.*

interface ApiService {

    @Headers("X-API-KEY: $API_KEY", "Cache-Control: max-age=$CACHE_MAX_AGE_SECONDS")
    @GET("top?type=TOP_100_POPULAR_FILMS")
    suspend fun getTopPopularFilms(@Query(QUERY_PARAM_PAGE) page: Int): ResponseDto

    @Headers("X-API-KEY: $API_KEY", "Cache-Control: max-age=$CACHE_MAX_AGE_SECONDS")
    @GET("top/{$QUERY_PATH_ID}")
    suspend fun getFilmDescription(@Path(QUERY_PATH_ID) id: Int): FilmDescriptionDto

    companion object {
        private const val QUERY_PARAM_PAGE = "page"
        private const val QUERY_PATH_ID = "id"
        private const val CACHE_MAX_AGE_SECONDS = 36000

        private const val API_KEY = "d985726c-2563-4b5a-bed8-0857d1b8944c"
        // key from tinkoff
        // private const val API_KEY = "e30ffed0-76ab-4dd6-b41f-4c9da2b2735b"
    }
}