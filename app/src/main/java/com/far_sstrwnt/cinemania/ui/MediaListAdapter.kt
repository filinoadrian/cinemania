package com.far_sstrwnt.cinemania.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.far_sstrwnt.cinemania.databinding.ItemMediaListBinding
import com.far_sstrwnt.cinemania.ui.home.HomeViewModel
import com.far_sstrwnt.cinemania.ui.home.MediaList
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MediaListAdapter(val viewModel: HomeViewModel) : ListAdapter<MediaList, MediaListAdapter.ViewHolder>(MediaListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemMediaListBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            (holder as ViewHolder).bind(viewModel, item)
        }
    }

    class ViewHolder(private val binding: ItemMediaListBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: HomeViewModel, mediaList: MediaList) {
            val mediaPagingAdapter = MediaPagingAdapter()

            binding.mediaListTitle.text = mediaList.title
            binding.mediaListItem.adapter = mediaPagingAdapter.withLoadStateHeaderAndFooter(
                header = EntityLoadStateAdapter { mediaPagingAdapter.retry() },
                footer = EntityLoadStateAdapter { mediaPagingAdapter.retry() }
            )

            viewModel.viewModelScope.launch {
                viewModel.fetchMediaByCategory(
                    mediaList.type.value,
                    mediaList.category.value
                ).collectLatest {
                    mediaPagingAdapter.submitData(it)
                }
            }
        }
    }
}

class MediaListDiffCallback: DiffUtil.ItemCallback<MediaList>() {
    override fun areItemsTheSame(oldItem: MediaList, newItem: MediaList): Boolean =
        oldItem.title == newItem.title

    override fun areContentsTheSame(oldItem: MediaList, newItem: MediaList): Boolean =
        oldItem == newItem
}