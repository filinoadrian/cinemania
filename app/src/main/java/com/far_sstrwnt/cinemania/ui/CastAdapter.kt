package com.far_sstrwnt.cinemania.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.far_sstrwnt.cinemania.databinding.ItemCastBinding
import com.far_sstrwnt.cinemania.model.CastEntity
import com.far_sstrwnt.cinemania.ui.common.EventActions

class CastAdapter(private val eventListener: EventActions)
    : ListAdapter<CastEntity, RecyclerView.ViewHolder>(CastDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val castItem = getItem(position)
        castItem?.let {
            (holder as ViewHolder).bind(eventListener, castItem)
        }
    }

    class ViewHolder private constructor(val binding: ItemCastBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(eventListener: EventActions, cast: CastEntity) {
            binding.eventListener = eventListener
            binding.cast = cast
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemCastBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

class CastDiffCallback: DiffUtil.ItemCallback<CastEntity>() {
    override fun areItemsTheSame(oldItem: CastEntity, newItem: CastEntity): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: CastEntity, newItem: CastEntity): Boolean =
        oldItem == newItem
}