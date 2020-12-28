package com.far_sstrwnt.cinemania.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.far_sstrwnt.cinemania.databinding.ItemMediaBinding
import com.far_sstrwnt.cinemania.model.MediaEntity

class MediaPagingAdapter : PagingDataAdapter<MediaEntity, RecyclerView.ViewHolder>(MediaDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemMediaBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            (holder as ViewHolder).bind(item)
        }
    }

    class ViewHolder(private val binding: ItemMediaBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(media: MediaEntity) {
            binding.media = media
            binding.executePendingBindings()
        }
    }
}

class MediaDiffCallback: DiffUtil.ItemCallback<MediaEntity>() {
    override fun areItemsTheSame(oldItem: MediaEntity, newItem: MediaEntity): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: MediaEntity, newItem: MediaEntity): Boolean =
        oldItem == newItem
}