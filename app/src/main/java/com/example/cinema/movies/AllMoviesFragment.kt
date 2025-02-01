package com.example.cinema.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cinema.core_network.Movie
import com.example.cinema.databinding.FragmentAllMoviesBinding


class AllMoviesFragment : Fragment() {

    private var _binding: FragmentAllMoviesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AllMoviesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sectionTitle = arguments?.getString("sectionTitle") ?: "Все фильмы"
        val countryId = arguments?.getInt("countryId")
        val genreId = arguments?.getInt("genreId")

        binding.sectionTitleTextView.text = sectionTitle

        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }

        val moviesAdapter = MoviesAdapter(
            emptyList(),
            onMovieClick = { movie ->
                openMovieDetailsFragment(movie)
            },
            onButtonClick = {
                Toast.makeText(context, "Show all movies", Toast.LENGTH_SHORT).show()
            }
        )

        binding.moviesRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = moviesAdapter
        }

        viewModel.getAllMovies(sectionTitle, countryId, genreId).observe(viewLifecycleOwner) { movies ->
            moviesAdapter.updateMovies(movies)
        }
    }
    private fun openMovieDetailsFragment(movie: Movie) {
        val action = AllMoviesFragmentDirections.actionAllMoviesToMovieDetails(movie.id)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}