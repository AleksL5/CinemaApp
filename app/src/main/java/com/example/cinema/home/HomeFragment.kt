package com.example.cinema.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.cinema.core_network.Movie
import com.example.cinema.R
import com.example.cinema.databinding.FragmentHomeBinding
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var sectionsAdapter: SectionsAdapter
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Инициализация адаптера для разделов
        sectionsAdapter = SectionsAdapter(
            sections = emptyList(),
            onMovieClick = { movie -> openMovieDetailsFragment(movie) }, // Обработчик кликов на фильм
            onButtonClick = { section -> openAllMoviesFragment(section) } // Обработчик кликов на кнопку "See All"
        )
        binding.sectionRecyclerView.adapter = sectionsAdapter

        // Подписка на данные из ViewModel
        observeSections()

        // Загружаем данные, если они ещё не загружены
        if (viewModel.sectionsLiveData.value == null) {
            viewModel.loadSections()
        }
    }

    private fun observeSections() {
        viewModel.sectionsLiveData.observe(viewLifecycleOwner) { sections ->
            updateUI(sections)
        }
    }

    private fun openAllMoviesFragment(section: Section) {
        val action = HomeFragmentDirections.actionHomeToAllMovies(
            sectionTitle = section.title,
            countryId = section.countryId ?: -1, // Передаём -1, если countryId отсутствует
            genreId = section.genreId ?: -1     // Передаём -1, если genreId отсутствует
        )

        // Используем NavOptions для предотвращения пересоздания HomeFragment
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.homeFragment, false)
            .build()

        findNavController().navigate(action, navOptions)
    }
    private fun openMovieDetailsFragment(movie: Movie) {
        val action = HomeFragmentDirections.actionHomeToMovieDetails(movie.id)
        findNavController().navigate(action)
    }

    private fun updateUI(sections: List<Section>) {
        sectionsAdapter.updateSections(sections)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}