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
import com.far_sstrwnt.cinemania.ui.EntityLoadStateAdapter
import com.far_sstrwnt.cinemania.ui.movies.MoviesPagingAdapter
import com.far_sstrwnt.cinemania.ui.toVisibility
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class SearchMoviePagerFragment(private val viewModel: SearchViewModel) : Fragment() {

    private lateinit var binding: FragmentSearchPagerBinding

    private lateinit var adapter: MoviesPagingAdapter

    private var searchJob: Job? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentSearchPagerBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.lifecycleOwner = this.viewLifecycleOwner

        initAdapter()

        viewModel.currentQueryValue.observe(this.viewLifecycleOwner, Observer {
            searchMovie(it)
        })
    }

    private fun searchMovie(query: String) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchMovie(query).collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun initAdapter() {
        adapter = MoviesPagingAdapter(
            viewModel,
            R.layout.item_movie_search
        )
        binding.searchList.adapter = adapter.withLoadStateHeaderAndFooter(
            header = EntityLoadStateAdapter { adapter.retry() },
            footer = EntityLoadStateAdapter { adapter.retry() }
        )
        adapter.addLoadStateListener { loadState ->
            if (loadState.refresh !is LoadState.NotLoading) {
                binding.searchList.visibility = View.GONE
                binding.progressBar.visibility = toVisibility(loadState.refresh is LoadState.Loading)
                binding.retryButton.visibility = toVisibility(loadState.refresh is LoadState.Error)
            } else {
                binding.searchList.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
                binding.retryButton.visibility = View.GONE

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