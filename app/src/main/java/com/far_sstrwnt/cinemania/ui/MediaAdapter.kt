package com.far_sstrwnt.cinemania.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.far_sstrwnt.cinemania.databinding.ItemTrendingBinding
import com.far_sstrwnt.cinemania.model.MediaEntity
import com.far_sstrwnt.cinemania.ui.common.MediaActionsHandler

class MediaAdapter(
    val mediaType: String,
    val eventListener: MediaActionsHandler,
) : RecyclerView.Adapter<MediaAdapter.ViewHolder>() {

    private var mMediaList: List<MediaEntity> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemTrendingBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mMediaList[position]
        item.let {
            holder.bind(mediaType, eventListener, item)
        }
    }

    override fun getItemCount(): Int {
        return mMediaList.size
    }

    fun setShowList(mediaList: List<MediaEntity>) {
        mMediaList = mediaList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemTrendingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(mediaType: String, eventListener: MediaActionsHandler, media: MediaEntity) {
            binding.mediaType = mediaType
            binding.eventListener = eventListener
            binding.media = media
            binding.executePendingBindings()
        }
    }
}