package com.far_sstrwnt.cinemania.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.far_sstrwnt.cinemania.BR
import com.far_sstrwnt.cinemania.databinding.ItemMediaTrendingBinding
import com.far_sstrwnt.cinemania.model.MediaEntity
import com.far_sstrwnt.cinemania.model.MediaType
import com.far_sstrwnt.cinemania.ui.common.MediaActionsHandler
import com.far_sstrwnt.cinemania.ui.favorites.FavoriteViewModel

class MediaAdapter(
    val mediaType: String,
    val eventListener: MediaActionsHandler,
    @LayoutRes val layoutId: Int
) : RecyclerView.Adapter<MediaAdapter.ViewHolder>() {

    private var mMediaList: List<MediaEntity> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, layoutId,  parent, false)
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

    fun setMediaList(mediaList: List<MediaEntity>) {
        mMediaList = mediaList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(mediaType: String, eventListener: MediaActionsHandler, media: MediaEntity) {
            val type = if (media.originalTitle != null) {
                MediaType.MOVIE.value
            } else {
                MediaType.TV.value
            }

            binding.setVariable(BR.mediaType, type)
            binding.setVariable(BR.eventListener, eventListener)
            binding.setVariable(BR.media, media)
            binding.executePendingBindings()
        }
    }
}