package com.example.cinema.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.cinema.core_network.ApiClient
import com.example.cinema.core_network.ApiService
import com.example.cinema.databinding.FragmentSearchSettingsBinding
import kotlinx.coroutines.launch

class SearchSettingsFragment : Fragment() {

    companion object {
        var isFirstLaunch = true
    }

    private var _binding: FragmentSearchSettingsBinding? = null
    private val binding get() = _binding!!

    private val searchViewModel: SearchViewModel by viewModels {
        SavedStateViewModelFactory(requireActivity().application, this)
    }

    private val apiService by lazy {
        ApiClient.retrofit.create(ApiService::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isFirstLaunch) {
            clearSavedSelections() // Очищаем данные только при первом запуске
            isFirstLaunch = false
        }

        // Загружаем фильтры
        loadFilters()

        setupSpinnerListeners()

        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }

        // Восстанавливаем выбранные значения
        restoreSpinnerSelections()
    }

    private fun loadFilters() {
        lifecycleScope.launch {
            try {
                val filtersResponse = apiService.getFilters()

                // Добавляем пустой элемент в список стран
                val countryNames = listOf("   ") + filtersResponse.countries.map { it.name }
                val countryAdapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    countryNames
                )
                countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.countrySpinner.adapter = countryAdapter

                // Добавляем пустой элемент в список жанров
                val genreNames = listOf("   ") + filtersResponse.genres.map { it.name }
                val genreAdapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    genreNames
                )
                genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.genreSpinner.adapter = genreAdapter

                // Восстанавливаем выбранные значения после загрузки адаптеров
                restoreSpinnerSelections()

            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Не удалось загрузить фильтры", Toast.LENGTH_SHORT)
                    .show()
                Log.d("SearchSettingsFragment", "Error loading filters", e)
            }
        }
    }
    private fun setupSpinnerListeners() {
        val sharedPreferences = requireContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)

        binding.countrySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCountry = parent?.getItemAtPosition(position) as String

                // Логируем выбранную страну
                Log.d("SearchSettingsFragment", "Selected country: $selectedCountry")

                // Сохраняем выбор в SharedPreferences
                sharedPreferences.edit().putString("selected_country", selectedCountry).apply()

                // Передаем значение в ViewModel
                searchViewModel.setSelectedCountry(selectedCountry)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.d("SearchSettingsFragment", "No country selected")
            }
        }

        binding.genreSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedGenre = parent?.getItemAtPosition(position) as String

                // Логируем выбранный жанр
                Log.d("SearchSettingsFragment", "Selected genre: $selectedGenre")

                // Сохраняем выбор в SharedPreferences
                sharedPreferences.edit().putString("selected_genre", selectedGenre).apply()

                // Передаем значение в ViewModel
                searchViewModel.setSelectedGenre(selectedGenre)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.d("SearchSettingsFragment", "No genre selected")
            }
        }
    }

    private fun restoreSpinnerSelections() {
        val sharedPreferences = requireContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)

        // Восстанавливаем сохранённое значение для страны
        val savedCountry = sharedPreferences.getString("selected_country", null)
        Log.d("SearchSettingsFragment", "Restoring selected country: $savedCountry")
        savedCountry?.let {
            val countryAdapter = binding.countrySpinner.adapter as? ArrayAdapter<String>
            val countryPosition = countryAdapter?.getPosition(it)
            Log.d("SearchSettingsFragment", "Country position: $countryPosition")
            countryPosition?.let { position ->
                binding.countrySpinner.setSelection(position)
            }
            searchViewModel.setSelectedCountry(it)  // Сохраняем в ViewModel
        }

        // Восстанавливаем сохранённое значение для жанра
        val savedGenre = sharedPreferences.getString("selected_genre", null)
        Log.d("SearchSettingsFragment", "Restoring selected genre: $savedGenre")
        savedGenre?.let {
            val genreAdapter = binding.genreSpinner.adapter as? ArrayAdapter<String>
            val genrePosition = genreAdapter?.getPosition(it)
            Log.d("SearchSettingsFragment", "Genre position: $genrePosition")
            genrePosition?.let { position ->
                binding.genreSpinner.setSelection(position)
            }
            searchViewModel.setSelectedGenre(it)  // Сохраняем в ViewModel
        }
    }
    private fun clearSavedSelections() {
        val sharedPreferences = requireContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
//class SearchSettingsFragment : Fragment() {
//
//    private var _binding: FragmentSearchSettingsBinding? = null
//    private val binding get() = _binding!!
//
//    private val searchViewModel: SearchViewModel by activityViewModels()
//
//    private val apiService by lazy {
//        ApiClient.retrofit.create(ApiService::class.java)
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentSearchSettingsBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        // Загружаем фильтры
//        loadFilters()
//
//        setupSpinnerListeners()
//    }
//
//    private fun loadFilters() {
//        lifecycleScope.launch {
//            try {
//                val filtersResponse = apiService.getFilters()
//
//                // Добавляем пустой элемент в список стран
//                val countryNames = listOf("   ") + filtersResponse.countries.map { it.name }
//                val countryAdapter = ArrayAdapter(
//                    requireContext(),
//                    android.R.layout.simple_spinner_item,
//                    countryNames
//                )
//                countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//                binding.countrySpinner.adapter = countryAdapter
//
//                // Добавляем пустой элемент в список жанров
//                val genreNames = listOf("   ") + filtersResponse.genres.map { it.name }
//                val genreAdapter = ArrayAdapter(
//                    requireContext(),
//                    android.R.layout.simple_spinner_item,
//                    genreNames
//                )
//                genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//                binding.genreSpinner.adapter = genreAdapter
//
//            } catch (e: Exception) {
//                Toast.makeText(requireContext(), "Не удалось загрузить фильтры", Toast.LENGTH_SHORT)
//                    .show()
//            }
//        }
//    }
//    private fun setupSpinnerListeners() {
//        val sharedPreferences = requireContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
//
//        binding.countrySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                val selectedCountry = parent?.getItemAtPosition(position) as String
//
//                // Сохраняем выбор в SharedPreferences
//                sharedPreferences.edit().putString("selected_country", selectedCountry).apply()
//
//                // Передаем значение в ViewModel
//                searchViewModel.selectedCountry.value = selectedCountry
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//                // Обработка, если ничего не выбрано (опционально)
//            }
//        }
//
//        binding.genreSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                val selectedGenre = parent?.getItemAtPosition(position) as String
//
//                // Сохраняем выбор в SharedPreferences
//                sharedPreferences.edit().putString("selected_genre", selectedGenre).apply()
//
//                // Передаем значение в ViewModel
//                searchViewModel.selectedGenre.value = selectedGenre
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//                // Обработка, если ничего не выбрано (опционально)
//            }
//        }
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}