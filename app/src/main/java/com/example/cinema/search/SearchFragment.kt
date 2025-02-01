package com.example.cinema.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cinema.core_network.Movie
import com.example.cinema.R
import com.example.cinema.movies.MoviesAdapter

class SearchFragment : Fragment() {

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var searchEditText: EditText
    private lateinit var resultsRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        searchEditText = view.findViewById(R.id.searchEditText)
        resultsRecyclerView = view.findViewById(R.id.resultsRecyclerView)

        resultsRecyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = MoviesAdapter(emptyList(), onMovieClick = { movie ->
            openMovieDetails(movie)
        }, onButtonClick = {
            // Handle load more results
        })
        resultsRecyclerView.adapter = adapter

        searchViewModel.searchResults.observe(viewLifecycleOwner) { results ->
            if (results.isEmpty()) {
                Toast.makeText(context, "Ничего не найдено. Попробуйте другой запрос.", Toast.LENGTH_SHORT).show()
            } else {
                adapter.updateMovies(results)
            }
        }



                // Добавляем обработчик касания для EditText
        searchEditText.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                // Получаем ширину EditText
                val editTextWidth = searchEditText.width
                val touchAreaStart = (editTextWidth * 5) / 6 // Левая граница последней 1/6 части

                // Если касание произошло в последней 1/6 части
                if (event.rawX >= touchAreaStart) {
                    findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToSearchSettingsFragment())
//                    Toast.makeText(context, "Нажата последняя часть строки!", Toast.LENGTH_SHORT).show()
                    return@setOnTouchListener true
                }
            }
            false
        }


        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    searchViewModel.searchMovies(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        return view
    }
    private fun openMovieDetails(movie: Movie) {
        val action = SearchFragmentDirections.actionSearchToMovieDetails(movie.id)
        findNavController().navigate(action)
    }
}

//class SearchFragment : Fragment() {
//
//    private lateinit var searchViewModel: SearchViewModel
//    private lateinit var searchEditText: EditText
//    private lateinit var resultsRecyclerView: RecyclerView
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.fragment_search, container, false)
//
//        searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
//        searchEditText = view.findViewById(R.id.searchEditText)
//        resultsRecyclerView = view.findViewById(R.id.resultsRecyclerView)
//
//        resultsRecyclerView.layoutManager = LinearLayoutManager(context)
//        val adapter = MoviesAdapter(emptyList(), onMovieClick = { movie ->
//           openMovieDetails(movie)
//        }, onButtonClick = {
//            // Handle load more results
//        })
//        resultsRecyclerView.adapter = adapter
//
//        searchViewModel.searchResults.observe(viewLifecycleOwner) { results ->
//            if (results.isEmpty()) {
//                Toast.makeText(context, "Ничего не найдено. Попробуйте другой запрос.", Toast.LENGTH_SHORT).show()
//            } else {
//                adapter.updateMovies(results)
//            }
//        }
//
//        // Добавляем обработчик касания для EditText
//        searchEditText.setOnTouchListener { _, event ->
//            if (event.action == MotionEvent.ACTION_UP) {
//                // Получаем ширину EditText
//                val editTextWidth = searchEditText.width
//                val touchAreaStart = (editTextWidth * 5) / 6 // Левая граница последней 1/6 части
//
//                // Если касание произошло в последней 1/6 части
//                if (event.rawX >= touchAreaStart) {
//                    findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToSearchSettingsFragment())
////                    Toast.makeText(context, "Нажата последняя часть строки!", Toast.LENGTH_SHORT).show()
//                    return@setOnTouchListener true
//                }
//            }
//            false
//        }
//
//        searchEditText.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                if (!s.isNullOrEmpty()) {
//                    searchViewModel.searchMovies(s.toString())
//                }
//            }
//
//            override fun afterTextChanged(s: Editable?) {}
//        })
//
//        return view
//    }
//    private fun openMovieDetails(movie: Movie) {
//        val action = SearchFragmentDirections.actionSearchToMovieDetails(movie.id)
//        findNavController().navigate(action)
//    }
//}