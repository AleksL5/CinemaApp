package com.example.cinema.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinema.core_network.ApiClient
import com.example.cinema.core_network.ApiService
import com.example.cinema.core_network.MovieDetails
import com.example.cinema.core_data.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieDetailsViewModel : ViewModel() {

    private val repository = MovieRepository(ApiClient.retrofit.create(ApiService::class.java))

    private val _movieDetails = MutableLiveData<MovieDetails>()
    val movieDetails: LiveData<MovieDetails> get() = _movieDetails

    fun loadMovieDetails(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val movieDetailsResponse = repository.getMovieDetails(movieId)
                _movieDetails.postValue(movieDetailsResponse)
            } catch (e: Exception) {
                // Handle error (e.g., show error state or log the exception)
                _movieDetails.postValue(null)
            }
        }
    }
}