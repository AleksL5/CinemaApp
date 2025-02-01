package com.example.cinema.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinema.core_network.ApiClient
import com.example.cinema.core_network.ApiService
import com.example.cinema.core_network.Country
import com.example.cinema.core_network.FilterItem
import com.example.cinema.core_network.Genre
import com.example.cinema.core_data.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val repository = MovieRepository(ApiClient.retrofit.create(ApiService::class.java))
    private val _sectionsLiveData = MutableLiveData<List<Section>>()
    val sectionsLiveData: LiveData<List<Section>> = _sectionsLiveData


    fun loadSections() {
        viewModelScope.launch(Dispatchers.IO) {
            val sections = mutableListOf<Section>()

            try {


                val premieres = repository.getPremieres(2024, "November").items
                if (premieres.isNotEmpty()) {
                    sections.add(Section("Премьеры", premieres,null ,null))
                }

                val topMovies = repository.getTop250Movies("TOP_250_MOVIES", 1).items
                if (topMovies.isNotEmpty()) {
                    sections.add(Section("Топ-250 фильмов", topMovies, null, null))
                }

                val series = repository.getSeries("COMICS_THEME", 1).items
                if (series.isNotEmpty()) {
                    sections.add(Section("Фантастика", series, null, null))
                }

                val seriesList = repository.getMoviesByFilter(countryId = 1, genreId = 4).items
                if (seriesList.isNotEmpty()) {
                    sections.add(Section("Сериалы", seriesList, null, null))
                }

                // Создаём и добавляем две случайные секции
                addRandomSections(sections)



            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                // Устанавливаем секции в LiveData
                _sectionsLiveData.postValue(sections)
            }
        }
    }

    /**
     * Обрабатывает фильтры и добавляет две секции со случайными фильмами
     */
    private suspend fun addRandomSections(sections: MutableList<Section>) {
        try {
            // Загружаем фильтры
            val filtersResponse = repository.getFilters()
            val countries = filtersResponse.countries
            val genres = filtersResponse.genres

            // Создаем первую случайную секцию
            createRandomFilterSection(countries, genres, sections)

            // Создаем вторую случайную секцию
            createRandomFilterSection(countries, genres, sections)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Генерирует случайные фильтры и добавляет новую секцию с фильмами
     */
    private suspend fun createRandomFilterSection(
        countries: List<Country>,
        genres: List<Genre>,
        sections: MutableList<Section>
    ) {
        val randomFilter = generateRandomFilter(countries, genres)

        val randomMovies = repository.getMoviesByFilter(
            countryId = randomFilter.countryId,
            genreId = randomFilter.genreId
        ).items

        if (randomMovies.isNotEmpty()) {
            val countryName = countries.find { it.id == randomFilter.countryId }?.name
                ?: "Страна ${randomFilter.countryId}"
            val genreName = genres.find { it.id == randomFilter.genreId }?.name
                ?: "Жанр ${randomFilter.genreId}"

            sections.add(
                Section(
                    title = "$countryName  ${genreName.capitalize() }",
                    movies = randomMovies,
                    countryId = randomFilter.countryId,
                    genreId = randomFilter.genreId
                )
            )
        }
    }

    /**
     * Генерирует случайные значения фильтров
     */
    private fun generateRandomFilter(countries: List<Country>, genres: List<Genre>): FilterItem {
        val randomCountryId = if (countries.isNotEmpty()) countries.random().id else (1..4).random()
        val randomGenreId = if (genres.isNotEmpty()) genres.random().id else (1..4).random()
        return FilterItem(countryId = randomCountryId, genreId = randomGenreId)
    }
}