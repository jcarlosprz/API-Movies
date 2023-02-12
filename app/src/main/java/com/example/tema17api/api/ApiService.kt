package com.example.tema17api.api

import com.example.tema17api.models.GenreMovieDetailResponse
import com.example.tema17api.models.GenresResponse
import com.example.tema17api.models.MoviesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("genre/movie/list")
    fun getGenres(
        @Query("api_key") apikey: String = ApiRest.apiKey,
        @Query("language") language: String = ApiRest.language
    ): Call<GenresResponse>

    @GET("discover/movie")
    fun getMovie(
        @Query("with_genres") with_genres: String,
        @Query("api_key") apikey: String = ApiRest.apiKey,
        @Query("language") language: String = ApiRest.language,
    ): Call<MoviesResponse>
    @GET("movie/{movie_id}")

    fun getGenreMovie(
        @Path("movie_id") movie_id: String,
        @Query("api_key") apikey: String = ApiRest.apiKey,
        @Query("language") language: String = ApiRest.language,
    ): Call<GenreMovieDetailResponse>
}