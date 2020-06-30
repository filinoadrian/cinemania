package com.far_sstrwnt.cinemania.ui.tv

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import com.far_sstrwnt.cinemania.R
import com.far_sstrwnt.cinemania.databinding.FragmentTvBinding
import com.far_sstrwnt.cinemania.shared.result.EventObserver
import com.far_sstrwnt.cinemania.ui.EntityLoadStateAdapter
import com.far_sstrwnt.cinemania.ui.GridItemDecoration
import com.far_sstrwnt.cinemania.ui.toVisibility
import com.far_sstrwnt.cinemania.util.viewModelProvider
import com.google.android.material.chip.Chip
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
class TvFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: TvViewModel

    private lateinit var binding: FragmentTvBinding

    private lateinit var adapter: TvPagingAdapter

    private var tvJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = viewModelProvider(viewModelFactory)

        binding = FragmentTvBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initAdapter()
        initGenre()
        initNavigation()

        loadTv(null)

        binding.tvList.setHasFixedSize(true)
        val padding = resources.getDimensionPixelSize(R.dimen.padding_small)
        binding.tvList.addItemDecoration(GridItemDecoration(padding))
        binding.retryButton.setOnClickListener { adapter.retry() }
    }

    private fun initAdapter() {
        adapter = TvPagingAdapter(viewModel, R.layout.item_tv_grid)
        binding.tvList.adapter = adapter.withLoadStateHeaderAndFooter(
            header = EntityLoadStateAdapter { adapter.retry() },
            footer = EntityLoadStateAdapter { adapter.retry() }
        )
        adapter.addLoadStateListener { loadState ->
            if (loadState.refresh !is LoadState.NotLoading) {
                binding.tvList.visibility = View.GONE
                binding.progressBar.visibility =
                    toVisibility(loadState.refresh is LoadState.Loading)
                binding.retryButton.visibility =
                    toVisibility(loadState.refresh is LoadState.Error)
            } else {
                binding.tvList.visibility = View.VISIBLE
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

    private fun initGenre() {

        viewModel.genreList.observe(viewLifecycleOwner, Observer { genreList ->
            val chipGroup = binding.genreList
            val inflater = LayoutInflater.from(chipGroup.context)

            val children = genreList.map { genre ->
                val chip = inflater.inflate(R.layout.item_genre, chipGroup, false) as Chip
                chip.text = genre.name
                chip.tag = genre.name
                chip.setOnClickListener {
                    updateTvListFromInput(genre.id)
                }
                chip
            }

            chipGroup.removeAllViews()

            for (chip in children) {
                chipGroup.addView(chip)
            }
        })

        lifecycleScope.launch {
            @OptIn(ExperimentalPagingApi::class)
            adapter.dataRefreshFlow.collect {
                binding.tvList.scrollToPosition(0)
            }
        }
    }

    private fun loadTv(genre: String?) {
        tvJob?.cancel()
        tvJob = lifecycleScope.launch {
            viewModel.discoverTv(genre).collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun updateTvListFromInput(genre: String) {
        binding.genreList.checkedChipId.let {
            binding.tvList.scrollToPosition(0)
            loadTv(genre)
        }
    }

    private fun initNavigation() {
        viewModel.navigateToTvDetailAction.observe(this.viewLifecycleOwner, EventObserver {
            openTvDetail(it)
        })
    }

    private fun openTvDetail(id: String) {
//        val action = MoviesFragmentDirections.actionNavMovieToNavMovieDetail(id)
//        findNavController().navigate(action)
    }
}