package com.far_sstrwnt.cinemania.ui.tv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.far_sstrwnt.cinemania.BR
import com.far_sstrwnt.cinemania.model.TvEntity
import com.far_sstrwnt.cinemania.ui.common.EventActions

class TvPagingAdapter(private val eventListener: EventActions, @LayoutRes val layoutId: Int) :
    PagingDataAdapter<TvEntity, RecyclerView.ViewHolder>(TV_COMPARATOR){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, layoutId, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val tvItem = getItem(position)
        tvItem?.let {
            (holder as ViewHolder).bind(eventListener, tvItem)
        }
    }

    class ViewHolder(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(eventListener: EventActions, tv: TvEntity) {
            binding.setVariable(BR.eventListener, eventListener)
            binding.setVariable(BR.tv, tv)
            binding.executePendingBindings()
        }
    }

    companion object {
        private val TV_COMPARATOR = object : DiffUtil.ItemCallback<TvEntity>() {
            override fun areItemsTheSame(oldItem: TvEntity, newItem: TvEntity): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: TvEntity, newItem: TvEntity): Boolean =
                oldItem == newItem
        }
    }
}