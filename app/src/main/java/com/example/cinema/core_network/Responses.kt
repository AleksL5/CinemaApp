package com.example.cinema.core_network

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

data class MovieResponse(val items: List<Movie>)


@Parcelize
data class Movie(
    @SerializedName("kinopoiskId") val id: Int,
    @SerializedName("nameRu") val title: String?,
    @SerializedName("genres") val genres: @RawValue List<Genre>,
    @SerializedName("posterUrl") val posterUrl: String,
    @SerializedName("countries") val countries: @RawValue List<Country>
) : Parcelable

data class FiltersResponse(
    @SerializedName("genres") val genres: List<Genre>,
    @SerializedName("countries") val countries: List<Country>
)

data class Genre(
    @SerializedName("id") val id: Int,
    @SerializedName("genre") val name: String
)

data class Country(
    @SerializedName("id") val id: Int,
    @SerializedName("country") val name: String
)

data class FilterItem(
    val countryId: Int,
    val genreId: Int
)
data class MovieDetails(
    val kinopoiskId: Int,
    val nameRu: String?,
    val nameEn: String?,
    val nameOriginal: String?,
    val posterUrl: String?,
    val description: String?,
    val year: Int?,
    val ratingKinopoisk: Double?,
    val countries: List<Country>?,
    val genres: List<Genre>?
)
