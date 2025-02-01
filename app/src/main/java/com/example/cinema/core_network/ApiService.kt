package com.example.cinema.core_network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("api/v2.2/films/premieres")
    suspend fun getPremieres(
        @Query("year") year: Int,
        @Query("month") month: String
    ): MovieResponse

    @GET("api/v2.2/films/collections")
    suspend fun getTopCollection(
        @Query("type") type: String,
        @Query("page") page: Int = 1
    ): MovieResponse

    @GET("api/v2.2/films")
    suspend fun getMoviesByFilter(
        @Query("countries") countryId: Int,
        @Query("genres") genreId: Int,
    ): MovieResponse

    @GET("api/v2.2/films/filters")
    suspend fun getFilters(): FiltersResponse

    @GET("api/v2.2/films")
    suspend fun searchMovies(
        @Query("keyword") query: String,
        @Query("country") country: String? = null,
        @Query("genre") genre: String? = null
    ): MovieResponse

    @GET("/api/v2.2/films/{id}")
    suspend fun getMovieDetails(@Path("id") id: Int): MovieDetails

}