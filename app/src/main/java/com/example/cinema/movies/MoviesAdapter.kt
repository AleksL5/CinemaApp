package com.example.cinema.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cinema.core_network.Movie
import com.example.cinema.R
import com.example.cinema.databinding.ItemButtonBinding
import com.example.cinema.databinding.ItemMovieBinding

class MoviesAdapter(
    private var movies: List<Movie>,
    private val maxVisibleItems: Int = 20,
    private val onMovieClick: (Movie) -> Unit,
    private val onButtonClick: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_MOVIE = 0
        private const val TYPE_BUTTON = 1
    }

    fun updateMovies(newMovies: List<Movie>) {
        movies = newMovies
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (movies.size > maxVisibleItems) maxVisibleItems + 1 else movies.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == maxVisibleItems && movies.size > maxVisibleItems) TYPE_BUTTON else TYPE_MOVIE
    }

    inner class MovieViewHolder(val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root)

    inner class ButtonViewHolder(val binding: ItemButtonBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_MOVIE -> {
                val binding = ItemMovieBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                MovieViewHolder(binding)
            }
            TYPE_BUTTON -> {
                val binding = ItemButtonBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                ButtonViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MovieViewHolder) {
            val movie = movies[position]
            holder.binding.movieTitle.text = movie.title ?: "Название отсутствует"
            holder.binding.movieGenre.text = movie.genres.joinToString(", ") { it.name }

            Glide.with(holder.itemView.context)
                .load(movie.posterUrl)
                .placeholder(R.drawable.image_placeholder)
                .into(holder.binding.moviePoster)

            holder.binding.root.setOnClickListener {
                onMovieClick(movie)
            }
        } else if (holder is ButtonViewHolder) {
            holder.binding.root.setOnClickListener {
                onButtonClick()
            }
        }
    }
}
