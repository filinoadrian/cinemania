package com.far_sstrwnt.cinemania.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.far_sstrwnt.cinemania.databinding.ItemEpisodeBinding
import com.far_sstrwnt.cinemania.model.EpisodeEntity

class EpisodeAdapter : ListAdapter<EpisodeEntity, EpisodeAdapter.ViewHolder>(EpisodeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemEpisodeBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        item.let {
            holder.bind(item)
        }
    }

    class ViewHolder(val binding: ItemEpisodeBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(episode: EpisodeEntity) {
            binding.episode = episode
            binding.executePendingBindings()
        }
    }
}

class EpisodeDiffCallback: DiffUtil.ItemCallback<EpisodeEntity>() {
    override fun areItemsTheSame(oldItem: EpisodeEntity, newItem: EpisodeEntity): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: EpisodeEntity, newItem: EpisodeEntity): Boolean =
        oldItem == newItem
}