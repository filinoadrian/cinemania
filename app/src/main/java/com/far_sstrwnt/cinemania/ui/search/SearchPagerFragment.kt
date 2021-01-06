package com.far_sstrwnt.cinemania.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.far_sstrwnt.cinemania.R
import com.far_sstrwnt.cinemania.databinding.FragmentSearchPagerBinding
import com.far_sstrwnt.cinemania.model.GenreEntity
import com.far_sstrwnt.cinemania.model.MediaType
import com.far_sstrwnt.cinemania.ui.adapter.EntityLoadStateAdapter
import com.far_sstrwnt.cinemania.ui.adapter.MediaPagingAdapter
import com.far_sstrwnt.cinemania.ui.home.HomeFragmentDirections
import com.far_sstrwnt.cinemania.util.setupSnackbar
import com.far_sstrwnt.cinemania.util.toVisibility
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
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

        viewDataBinding = FragmentSearchPagerBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }

        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner

        initAdapter()
        initSnackbar()

        viewModel.fetchMediaGenre(mediaType.value)

        if (mediaType.value == MediaType.MOVIE.value) {
            viewModel.movieGenre.observe(this.viewLifecycleOwner, {
                initGenre(it)
            })
        } else {
            viewModel.tvGenre.observe(this.viewLifecycleOwner, {
                initGenre(it)
            })
        }

        viewModel.currentQueryValue.observe(this.viewLifecycleOwner, {
            searchMedia(it)
        })
    }

    private fun initGenre(genreList: List<GenreEntity>) {
        val chipGroup = viewDataBinding.genreList
        val inflater = LayoutInflater.from(chipGroup.context)

        val children = genreList.map { genre ->
            val chip = inflater.inflate(R.layout.item_genre, chipGroup, false) as Chip
            chip.text = genre.name
            chip.tag = genre.name
            chip.setOnClickListener {
                val directions = SearchFragmentDirections.actionNavSearchToNavMedia(mediaType.value, genre.name, genre.id)
                findNavController().navigate(directions)
            }
            chip
        }

        chipGroup.removeAllViews()

        for (chip in children) {
            chipGroup.addView(chip)
        }
    }

    private fun initSnackbar() {
        view?.setupSnackbar(this, viewModel.snackbarMessage, Snackbar.LENGTH_SHORT)
    }

    private fun searchMedia(query: String) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.fetchMediaSearch(mediaType.value, query).collectLatest {
                if (query.length >= 3) {
                    adapter.submitData(it)
                } else {
                    adapter.submitData(PagingData.empty())
                }
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