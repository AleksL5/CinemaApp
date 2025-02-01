package com.example.cinema.core_data

import com.example.cinema.core_network.ApiService

class MovieRepository(private val api: ApiService) {

    suspend fun getPremieres(year: Int, month: String) = api.getPremieres(year, month)

    suspend fun getTop250Movies(type: String, page: Int) = api.getTopCollection(type = type, page = page)

    suspend fun getSeries(type: String, page: Int) = api.getTopCollection(type = type, page = page)

    suspend fun getMoviesByFilter(countryId: Int, genreId: Int) = api.getMoviesByFilter(countryId, genreId)

    suspend fun getFilters() = api.getFilters()

    suspend fun getMovieDetails(movieId: Int) = api.getMovieDetails(movieId)


}