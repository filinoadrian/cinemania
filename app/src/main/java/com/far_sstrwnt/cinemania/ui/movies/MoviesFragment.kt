package com.far_sstrwnt.cinemania.ui.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.far_sstrwnt.cinemania.R
import com.far_sstrwnt.cinemania.databinding.FragmentMoviesBinding
import com.far_sstrwnt.cinemania.ui.MovieGridItemDecoration
import com.far_sstrwnt.cinemania.ui.MoviesAdapter
import com.far_sstrwnt.cinemania.ui.MoviesLoadStateAdapter
import com.far_sstrwnt.cinemania.ui.toVisibility
import com.far_sstrwnt.cinemania.util.viewModelProvider
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
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
        loadMovies()

        binding.moviesList.setHasFixedSize(true)
        val largePadding = resources.getDimensionPixelSize(R.dimen.padding_normal)
        val smallPadding = resources.getDimensionPixelSize(R.dimen.padding_extra_small)
        binding.moviesList.addItemDecoration(MovieGridItemDecoration(largePadding, smallPadding))
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

    private fun loadMovies() {
        moviesJob?.cancel()
        moviesJob = lifecycleScope.launch {
            viewModel.discoverMovie().collectLatest {
                adapter.submitData(it)
            }
        }
    }
}