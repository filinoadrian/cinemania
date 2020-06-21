package com.far_sstrwnt.cinemania.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.far_sstrwnt.cinemania.BR
import com.far_sstrwnt.cinemania.model.MovieEntity

class MoviesAdapter(@LayoutRes val layoutId: Int) :
    PagingDataAdapter<MovieEntity, RecyclerView.ViewHolder>(MOVIE_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, layoutId, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val movieItem = getItem(position)
        movieItem?.let {
            (holder as ViewHolder).bind(movieItem)
        }
    }

    class ViewHolder(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: MovieEntity) {
            binding.setVariable(BR.movie, movie)
            binding.executePendingBindings()
        }
    }

    companion object {
        private val MOVIE_COMPARATOR = object : DiffUtil.ItemCallback<MovieEntity>() {
            override fun areItemsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean =
                oldItem == newItem
        }
    }
}