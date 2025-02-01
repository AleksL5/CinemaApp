package com.example.cinema.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinema.core_network.ApiClient
import com.example.cinema.core_network.ApiService
import com.example.cinema.core_network.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Сохраняем значения через SavedStateHandle
    val selectedCountry: MutableLiveData<String> = savedStateHandle.getLiveData("selectedCountry", "")
    val selectedGenre: MutableLiveData<String> = savedStateHandle.getLiveData("selectedGenre", "")

    private val _searchResults = MutableLiveData<List<Movie>>()
    val searchResults: LiveData<List<Movie>> get() = _searchResults
    

    // Метод для изменения значения страны
    fun setSelectedCountry(country: String) {
        selectedCountry.value = country
        savedStateHandle.set("selectedCountry", country)
    }

    // Метод для изменения значения жанра
    fun setSelectedGenre(genre: String) {
        selectedGenre.value = genre
        savedStateHandle.set("selectedGenre", genre)
    }

    fun searchMovies(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val results = ApiClient.retrofit.create(ApiService::class.java)
                    .searchMovies(query, selectedCountry.value ?: "", selectedGenre.value ?: "").items
                delay(2000)
                _searchResults.postValue(results)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

//class SearchViewModel : ViewModel() {
//
//    val selectedCountry = MutableLiveData<String>()
//    val selectedGenre = MutableLiveData<String>()
//
//    private val _searchResults = MutableLiveData<List<Movie>>()
//    val searchResults: LiveData<List<Movie>> get() = _searchResults
//
//    fun searchMovies(query: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val results = ApiClient.retrofit.create(ApiService::class.java)
//                    .searchMovies(query,selectedCountry.toString(), selectedGenre.toString()).items
//                delay(2000)
//                _searchResults.postValue(results)
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }
//}
