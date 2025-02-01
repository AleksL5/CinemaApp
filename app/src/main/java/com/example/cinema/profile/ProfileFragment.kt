package com.example.cinema.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.cinema.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        // Адаптеры для RecyclerView
//        val watchedMoviesAdapter = MoviesAdapter(emptyList())
//        val genreCollectionsAdapter = GenreAdapter(emptyList())
//
//        binding.watchedMoviesRecyclerView.adapter = watchedMoviesAdapter
//        binding.watchedMoviesRecyclerView.layoutManager =
//            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//
//        binding.genreCollectionsRecyclerView.adapter = genreCollectionsAdapter
//        binding.genreCollectionsRecyclerView.layoutManager =
//            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//
//        // Наблюдение за данными
//        viewModel.watchedMovies.observe(viewLifecycleOwner) { movies ->
//            watchedMoviesAdapter.updateData(movies)
//        }
//
//        viewModel.genreCollections.observe(viewLifecycleOwner) { collections ->
//            genreCollectionsAdapter.updateData(collections)
//        }
//
//        // Получение данных
//        viewModel.fetchData()
    }
}