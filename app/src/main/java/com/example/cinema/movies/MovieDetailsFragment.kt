package com.example.cinema.movies

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.cinema.core_network.MovieDetails
import com.example.cinema.R
import com.example.cinema.databinding.FragmentMovieDetailsBinding

class MovieDetailsFragment : Fragment() {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MovieDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }

        // Получение movieId из аргументов
        val movieId = arguments?.getInt("movieId")
        movieId?.let {
            viewModel.loadMovieDetails(it)
        }

        // Наблюдение за данными ViewModel
        viewModel.movieDetails.observe(viewLifecycleOwner) { details ->
            details?.let {
                updateUI(it)
            }
        }

        // Настройка клика для описания
        setupDescriptionToggle()
    }

    private fun updateUI(details: MovieDetails) {
        // Заголовок
        binding.movieTitle.text = details.nameRu ?: details.nameOriginal ?: getString(R.string.empty_data)

        // Описание
        binding.movieDescription.text = details.description ?: getString(R.string.empty_data)

        // Постер
        Glide.with(requireContext())
            .load(details.posterUrl)
            .placeholder(R.drawable.image_placeholder)
            .into(binding.moviePoster)

        // Рейтинг
        binding.movieRating.text = details.ratingKinopoisk?.let {
            getString(R.string.rating_format, it)
        } ?: getString(R.string.empty_data)

        // Жанры
        binding.movieGenres.text = details.genres?.takeIf { it.isNotEmpty() }
            ?.joinToString(", ") { it.name }
            ?.let { getString(R.string.movie_genres, it) }
            ?: getString(R.string.empty_data)

        // Страны
        binding.movieCountries.text = details.countries?.takeIf { it.isNotEmpty() }
            ?.joinToString(", ") { it.name }
            ?.let { getString(R.string.movie_countries, it) }
            ?: getString(R.string.empty_data)

        // Год
        binding.movieYear.text = details.year?.toString() ?: getString(R.string.unknown_year)
    }

    private fun setupDescriptionToggle() {
        var isExpanded = false // Переменная для отслеживания состояния

        binding.movieDescription.setOnClickListener {
            isExpanded = !isExpanded // Переключение состояния

            if (isExpanded) {
                // Развернуть текст
                binding.movieDescription.maxLines = Int.MAX_VALUE
                binding.movieDescription.ellipsize = null
            } else {
                // Свернуть текст
                binding.movieDescription.maxLines = 5
                binding.movieDescription.ellipsize = TextUtils.TruncateAt.END
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}