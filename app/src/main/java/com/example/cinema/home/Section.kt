package com.example.cinema.home

import com.example.cinema.core_network.Movie

data class Section(
    val title: String,
    val movies: List<Movie>,
    val countryId: Int? = null,
    val genreId: Int? = null
)