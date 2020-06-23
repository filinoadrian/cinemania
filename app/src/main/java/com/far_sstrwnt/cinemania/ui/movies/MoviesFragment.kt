package com.far_sstrwnt.cinemania.ui.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import com.far_sstrwnt.cinemania.R
import com.far_sstrwnt.cinemania.databinding.FragmentMoviesBinding
import com.far_sstrwnt.cinemania.ui.MovieGridItemDecoration
import com.far_sstrwnt.cinemania.ui.MoviesAdapter
import com.far_sstrwnt.cinemania.ui.MoviesLoadStateAdapter
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
class MoviesFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: MoviesViewModel

    private lateinit var binding: FragmentMoviesBinding

    private var adapter = MoviesAdapter(R.layout.item_movie_grid)

    private var moviesJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = viewModelProvider(viewModelFactory)

        binding = FragmentMoviesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initAdapter()
        loadMovies(null)
        initGenre()

        binding.moviesList.setHasFixedSize(true)
        val padding = resources.getDimensionPixelSize(R.dimen.padding_small)
        binding.moviesList.addItemDecoration(MovieGridItemDecoration(padding))
        binding.retryButton.setOnClickListener { adapter.retry() }
    }

    private fun initAdapter() {
        binding.moviesList.adapter = adapter.withLoadStateHeaderAndFooter(
            header = MoviesLoadStateAdapter { adapter.retry() },
            footer = MoviesLoadStateAdapter { adapter.retry() }
        )
        adapter.addLoadStateListener { loadState ->
            if (loadState.refresh !is LoadState.NotLoading) {
                binding.moviesList.visibility = View.GONE
                binding.progressBar.visibility =
                    toVisibility(loadState.refresh is LoadState.Loading)
                binding.retryButton.visibility =
                    toVisibility(loadState.refresh is LoadState.Error)
            } else {
                binding.moviesList.visibility = View.VISIBLE
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
                    updateMovieListFromInput(genre.id)
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
                binding.moviesList.scrollToPosition(0)
            }
        }
    }

    private fun loadMovies(genre: String?) {
        moviesJob?.cancel()
        moviesJob = lifecycleScope.launch {
            viewModel.discoverMovie(genre).collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun updateMovieListFromInput(genre: String) {
        binding.genreList.checkedChipId.let {
            binding.moviesList.scrollToPosition(0)
            loadMovies(genre)
        }
    }
}