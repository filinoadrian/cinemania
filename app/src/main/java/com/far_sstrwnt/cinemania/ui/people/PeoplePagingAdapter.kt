package com.far_sstrwnt.cinemania.ui.people

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.far_sstrwnt.cinemania.BR
import com.far_sstrwnt.cinemania.model.PeopleEntity
import com.far_sstrwnt.cinemania.ui.common.EventActionsHandler

class PeoplePagingAdapter(private val eventListener: EventActionsHandler, @LayoutRes val layoutId: Int) :
        PagingDataAdapter<PeopleEntity, RecyclerView.ViewHolder>(PEOPLE_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, layoutId, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val peopleItem = getItem(position)
        peopleItem?.let {
            (holder as ViewHolder).bind(eventListener, peopleItem)
        }
    }

    class ViewHolder(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(eventListener: EventActionsHandler, people: PeopleEntity) {
            binding.setVariable(BR.eventListener, eventListener)
            binding.setVariable(BR.people, people)
            binding.executePendingBindings()
        }
    }

    companion object {
        private val PEOPLE_COMPARATOR = object : DiffUtil.ItemCallback<PeopleEntity>() {
            override fun areItemsTheSame(oldItem: PeopleEntity, newItem: PeopleEntity): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: PeopleEntity, newItem: PeopleEntity): Boolean =
                oldItem == newItem
        }
    }
}