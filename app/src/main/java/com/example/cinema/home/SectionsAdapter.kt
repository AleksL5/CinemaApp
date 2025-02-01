package com.example.cinema.home

import android.graphics.Typeface
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cinema.core_network.Movie
import com.example.cinema.movies.MoviesAdapter
import com.example.cinema.databinding.ItemSectionBinding


class SectionsAdapter(
    private var sections: List<Section>,
    private val onMovieClick: (Movie) -> Unit,
    private val onButtonClick: (Section) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_SECTION = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) VIEW_TYPE_HEADER else VIEW_TYPE_SECTION
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_HEADER) {
            val textView = TextView(parent.context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                textSize = 25f
                setPadding(15, 0, 0, 110)
                gravity = Gravity.TOP
                typeface = Typeface.DEFAULT_BOLD
            }
            HeaderViewHolder(textView)
        } else {
            val binding = ItemSectionBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            SectionViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderViewHolder) {
            holder.textView.text = "Cinema"
        } else if (holder is SectionViewHolder) {
            val section = sections[position - 1]
            holder.binding.sectionTitle.text = section.title

            holder.binding.moviesRecyclerView.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = MoviesAdapter(
                    movies = section.movies,
                    maxVisibleItems = 10,
                    onMovieClick = { movie ->
                        onMovieClick(movie)
                    },
                    onButtonClick = {
                        onButtonClick(section)
                    }
                )
            }

            holder.binding.seeAllButton.setOnClickListener {
                onButtonClick(section)
            }
        }
    }

    override fun getItemCount(): Int {
        return sections.size + 1
    }

    fun updateSections(newSections: List<Section>) {
        sections = newSections
        notifyDataSetChanged()
    }

    inner class HeaderViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

    inner class SectionViewHolder(val binding: ItemSectionBinding) :
        RecyclerView.ViewHolder(binding.root)
}


//class SectionsAdapter(
//    private var sections: List<Section>,
//    private val onButtonClick: (Section) -> Unit
//) : RecyclerView.Adapter<SectionsAdapter.SectionViewHolder>() {
//
//    inner class SectionViewHolder(val binding: ItemSectionBinding) :
//        RecyclerView.ViewHolder(binding.root)
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {
//        val binding = ItemSectionBinding.inflate(
//            LayoutInflater.from(parent.context), parent, false
//        )
//        return SectionViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
//        val section = sections[position]
//
//        holder.binding.sectionTitle.text = section.title
//
//
//        holder.binding.moviesRecyclerView.apply {
//            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//            adapter = MoviesAdapter(
//                movies = section.movies,
//                maxVisibleItems = 10,
//                onMovieClick = { movie ->
//                    // Реакция на клик по фильму
//                    Toast.makeText(context, "Clicked on: ${movie.title}", Toast.LENGTH_SHORT).show()
//                },
//                onButtonClick = {
//                    // Реакция на нажатие кнопки "Показать все"
//                    onButtonClick(section)
//                }
//            )
//        }
//
//        holder.binding.seeAllButton.setOnClickListener {
//            // Реакция на кнопку "Все" в заголовке секции
//            onButtonClick(section)
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return sections.size
//    }
//
//    fun updateSections(newSections: List<Section>) {
//        sections = newSections
//        notifyDataSetChanged()
//    }
//}
