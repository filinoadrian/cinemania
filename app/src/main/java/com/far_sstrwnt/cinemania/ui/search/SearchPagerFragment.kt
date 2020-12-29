package com.far_sstrwnt.cinemania.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.far_sstrwnt.cinemania.R
import com.far_sstrwnt.cinemania.databinding.FragmentSearchPagerBinding
import com.far_sstrwnt.cinemania.model.MediaType
import com.far_sstrwnt.cinemania.ui.EntityLoadStateAdapter
import com.far_sstrwnt.cinemania.ui.MediaPagingAdapter
import com.far_sstrwnt.cinemania.ui.toVisibility
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class SearchPagerFragment(
    val mediaType: MediaType,
    val viewModel: SearchViewModel
) : Fragment() {

    private lateinit var viewDataBinding: FragmentSearchPagerBinding

    private lateinit var adapter: MediaPagingAdapter

    private var searchJob: Job? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        viewDataBinding = FragmentSearchPagerBinding.inflate(inflater, container, false)

        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner

        initAdapter()

        viewModel.currentQueryValue.observe(this.viewLifecycleOwner, Observer {
            searchMedia(it)
        })
    }

    private fun searchMedia(query: String) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.fetchMediaSearch(mediaType.value, query).collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun initAdapter() {
        adapter = MediaPagingAdapter(
            mediaType.value,
            viewModel,
            R.layout.item_media_search
        )
        viewDataBinding.searchList.adapter = adapter.withLoadStateHeaderAndFooter(
            header = EntityLoadStateAdapter { adapter.retry() },
            footer = EntityLoadStateAdapter { adapter.retry() }
        )
        adapter.addLoadStateListener { loadState ->
            if (loadState.refresh !is LoadState.NotLoading) {
                viewDataBinding.searchList.visibility = View.GONE
                viewDataBinding.progressBar.visibility = toVisibility(loadState.refresh is LoadState.Loading)
                viewDataBinding.retryButton.visibility = toVisibility(loadState.refresh is LoadState.Error)
            } else {
                viewDataBinding.searchList.visibility = View.VISIBLE
                viewDataBinding.progressBar.visibility = View.GONE
                viewDataBinding.retryButton.visibility = View.GONE

                val errorState = when {
                    loadState.append is LoadState.Error -> {
                        loadState.append as LoadState.Error
                    }
                    loadState.prepend is LoadState.Error -> {
                        loadState.prepend as LoadState.Error
                    }
                    else -> {
                        null
                    }
                }
                errorState?.let {
                    Toast.makeText(
                        requireActivity(),
                        "\uD83D\uDE28 Wooops ${it.error}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}