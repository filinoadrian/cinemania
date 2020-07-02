package com.far_sstrwnt.cinemania.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.far_sstrwnt.cinemania.databinding.ItemTvBinding
import com.far_sstrwnt.cinemania.model.TvEntity
import com.far_sstrwnt.cinemania.ui.common.EventActionsHandler

class TvAdapter(private val eventListener: EventActionsHandler)
    :ListAdapter<TvEntity, RecyclerView.ViewHolder>(TvDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val tvItem = getItem(position)
        tvItem?.let {
            (holder as ViewHolder).bind(eventListener, tvItem)
        }
    }

    class ViewHolder private constructor(val binding: ItemTvBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(eventListener: EventActionsHandler, tv: TvEntity) {
            binding.eventListener = eventListener
            binding.tv = tv
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemTvBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

class TvDiffCallback: DiffUtil.ItemCallback<TvEntity>() {
    override fun areItemsTheSame(oldItem: TvEntity, newItem: TvEntity): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: TvEntity, newItem: TvEntity): Boolean =
        oldItem == newItem

}