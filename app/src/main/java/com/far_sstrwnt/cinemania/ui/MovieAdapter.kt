package com.far_sstrwnt.cinemania.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.far_sstrwnt.cinemania.databinding.ItemMovieBinding
import com.far_sstrwnt.cinemania.model.MovieEntity
import com.far_sstrwnt.cinemania.ui.common.EventActionsHandler

class MovieAdapter(private val eventListener: EventActionsHandler)
    : ListAdapter<MovieEntity, RecyclerView.ViewHolder>(MovieDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val movieItem = getItem(position)
        movieItem?.let {
            (holder as ViewHolder).bind(eventListener, movieItem)
        }
    }

    class ViewHolder private constructor(val binding: ItemMovieBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(eventListener: EventActionsHandler, movie: MovieEntity) {
            binding.eventListener = eventListener
            binding.movie = movie
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemMovieBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

class MovieDiffCallback: DiffUtil.ItemCallback<MovieEntity>() {
    override fun areItemsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean =
        oldItem == newItem
}