package com.far_sstrwnt.cinemania.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.far_sstrwnt.cinemania.BR
import com.far_sstrwnt.cinemania.model.MediaEntity
import com.far_sstrwnt.cinemania.ui.common.MediaActionsHandler

class MediaPagingAdapter(
    val mediaType: String,
    val eventListener: MediaActionsHandler,
    @LayoutRes val layoutId: Int
) : PagingDataAdapter<MediaEntity, RecyclerView.ViewHolder>(MediaDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, layoutId, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            (holder as ViewHolder).bind(mediaType, eventListener, item)
        }
    }

    class ViewHolder(private val binding: ViewDataBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(mediaType: String, eventListener: MediaActionsHandler, media: MediaEntity) {
            binding.setVariable(BR.mediaType, mediaType)
            binding.setVariable(BR.eventListener, eventListener)
            binding.setVariable(BR.media, media)
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