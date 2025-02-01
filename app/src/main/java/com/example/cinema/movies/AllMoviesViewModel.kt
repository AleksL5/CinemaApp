package com.example.cinema.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.cinema.core_network.ApiClient
import com.example.cinema.core_network.ApiService
import com.example.cinema.core_network.Movie
import com.example.cinema.core_data.MovieRepository
import kotlinx.coroutines.Dispatchers

class AllMoviesViewModel : ViewModel() {

    private val repository = MovieRepository(ApiClient.retrofit.create(ApiService::class.java))

    fun getAllMovies(sectionTitle: String, countryId: Int? = null, genreId: Int? = null): LiveData<List<Movie>> = liveData(Dispatchers.IO) {
        try {
            val movies = when {
                // Случайный выбор фильмов с указанными фильтрами
                countryId != null && genreId != null && countryId != -1 && genreId != -1 ->
                    repository.getMoviesByFilter(countryId, genreId).items

                // Разделы без фильтров
                sectionTitle == "Премьеры" ->
                    repository.getPremieres(2024, "November").items

                sectionTitle == "Топ-250 фильмов" ->
                    repository.getTop250Movies("TOP_250_MOVIES", 1).items


                sectionTitle == "Фантастика" ->
                    repository.getSeries("COMICS_THEME", 1).items
                sectionTitle == "Сериалы" ->
                    repository.getMoviesByFilter(1, 4).items


                else -> emptyList()
            }
            emit(movies)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }
}