package com.far_sstrwnt.cinemania.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.far_sstrwnt.cinemania.databinding.ItemVideoBinding
import com.far_sstrwnt.cinemania.model.VideoEntity
import com.far_sstrwnt.cinemania.ui.common.VideoActionsHandler

class VideoAdapter(private val videoActionsHandler: VideoActionsHandler) :
    ListAdapter<VideoEntity, RecyclerView.ViewHolder>(VideoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val videoItem = getItem(position)
        videoItem?.let {
            (holder as ViewHolder).bind(videoActionsHandler, videoItem)
        }
    }

    class ViewHolder private constructor(val binding: ItemVideoBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(videoActionsHandler: VideoActionsHandler, video: VideoEntity) {
            binding.actionHandler = videoActionsHandler
            binding.video = video
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemVideoBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

class VideoDiffCallback: DiffUtil.ItemCallback<VideoEntity>() {
    override fun areItemsTheSame(oldItem: VideoEntity, newItem: VideoEntity): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: VideoEntity, newItem: VideoEntity): Boolean =
        oldItem == newItem
}