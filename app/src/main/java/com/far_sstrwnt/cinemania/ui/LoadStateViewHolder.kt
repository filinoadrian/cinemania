package com.far_sstrwnt.cinemania.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.far_sstrwnt.cinemania.R
import com.far_sstrwnt.cinemania.databinding.ItemLoadStateFooterBinding

class LoadStateViewHolder(
        private val binding: ItemLoadStateFooterBinding,
        retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.retryButton.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            binding.errorMsg.text = loadState.error.localizedMessage
        }
        binding.progressBar.visibility = toVisibility(loadState is LoadState.Loading)
        binding.retryButton.visibility = toVisibility(loadState !is LoadState.Loading)
        binding.errorMsg.visibility = toVisibility(loadState !is LoadState.Loading)
    }

    companion object {

        fun create(parent: ViewGroup, retry: () -> Unit): LoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_load_state_footer, parent, false)
            val binding = ItemLoadStateFooterBinding.bind(view)
            return LoadStateViewHolder(binding, retry)
        }
    }
}