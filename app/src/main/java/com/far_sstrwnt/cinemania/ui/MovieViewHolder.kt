package com.far_sstrwnt.cinemania.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.far_sstrwnt.cinemania.databinding.MovieViewItemBinding
import com.far_sstrwnt.cinemania.model.MovieEntity

class MovieViewHolder(val binding: MovieViewItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(movie: MovieEntity?) {
        binding.movie = movie
        binding.executePendingBindings()
    }

    companion object {
        fun create(parent: ViewGroup): MovieViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = MovieViewItemBinding.inflate(layoutInflater, parent, false)

            return MovieViewHolder(binding)
        }
    }
}