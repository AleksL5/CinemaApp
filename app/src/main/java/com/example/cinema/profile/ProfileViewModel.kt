package com.example.cinema.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cinema.core_network.Movie

class ProfileViewModel : ViewModel() {

    private val _watchedMovies = MutableLiveData<List<Movie>>()
    val watchedMovies: LiveData<List<Movie>> get() = _watchedMovies
//
//    private val _genreCollections = MutableLiveData<List<GenreCollection>>()
//    val genreCollections: LiveData<List<GenreCollection>> get() = _genreCollections

//    fun fetchData() {
//        viewModelScope.launch {
//            try {
//                val watched = ApiClient.apiService.getWatchedMovies()
//                val collections = ApiClient.apiService.getGenreCollections()
//                _watchedMovies.value = watched
//                _genreCollections.value = collections
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }
}