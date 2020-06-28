package com.far_sstrwnt.cinemania.ui.moviedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import com.far_sstrwnt.cinemania.R
import com.far_sstrwnt.cinemania.databinding.FragmentMovieDetailBinding
import com.far_sstrwnt.cinemania.shared.result.EventObserver
import com.far_sstrwnt.cinemania.ui.CastAdapter
import com.far_sstrwnt.cinemania.ui.MoviesLoadStateAdapter
import com.far_sstrwnt.cinemania.ui.MoviesPagingAdapter
import com.far_sstrwnt.cinemania.ui.toVisibility
import com.far_sstrwnt.cinemania.util.viewModelProvider
import com.google.android.material.appbar.AppBarLayout
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class MovieDetailFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: MovieDetailViewModel

    private lateinit var binding: FragmentMovieDetailBinding

    private lateinit var castAdapter: CastAdapter

    private lateinit var similarAdapter: MoviesPagingAdapter

    private val args: MovieDetailFragmentArgs by navArgs()

    private var movieSimilarJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = viewModelProvider(viewModelFactory)

        binding = FragmentMovieDetailBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.lifecycleOwner = this.viewLifecycleOwner

        initAppBar()
        initAdapter()
        initNavigation()

        viewModel.loadMovieDetail(args.id)
        viewModel.loadMovieCast(args.id)
        loadMovieSimilar(args.id)

        subscribeUi()

        binding.retryButton.setOnClickListener { similarAdapter.retry() }
    }

    private fun initAppBar() {
        val appCompatActivity = activity as AppCompatActivity
        appCompatActivity.setSupportActionBar(binding.toolbar)
        appCompatActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var isShow = true
        var scrollRange = -1
        binding.appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (scrollRange == -1) {
                scrollRange = appBarLayout?.totalScrollRange!!
            }
            if (scrollRange + verticalOffset == 0){
                binding.collapsingToolbar.title = viewModel.movie.value?.title
                isShow = true
            } else if (isShow){
                binding.collapsingToolbar.title = " "
                isShow = false
            }
        })
    }

    private fun initAdapter() {
        castAdapter = CastAdapter(viewModel)
        binding.movieCast.adapter = castAdapter

        similarAdapter = MoviesPagingAdapter(viewModel, R.layout.item_movie)
        binding.movieSimilarList.adapter = similarAdapter.withLoadStateHeaderAndFooter(
            header = MoviesLoadStateAdapter { similarAdapter.retry() },
            footer = MoviesLoadStateAdapter { similarAdapter.retry() }
        )
        similarAdapter.addLoadStateListener { loadState ->
            if (loadState.refresh !is LoadState.NotLoading) {
                binding.movieSimilarList.visibility = View.GONE
                binding.progressBar.visibility =
                    toVisibility(loadState.refresh is LoadState.Loading)
                binding.retryButton.visibility =
                    toVisibility(loadState.refresh is LoadState.Error)
            } else {
                binding.movieSimilarList.visibility = View.VISIBLE
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

    private fun loadMovieSimilar(id: String) {
        movieSimilarJob?.cancel()
        movieSimilarJob = lifecycleScope.launch {
            viewModel.loadMovieSimilar(id).collectLatest {
                similarAdapter.submitData(it)
            }
        }
    }

    private fun initNavigation() {
        viewModel.navigateToPeopleDetailAction.observe(this.viewLifecycleOwner, EventObserver {
            openPeopleDetail(it)
        })
        viewModel.navigateToMovieDetailAction.observe(this.viewLifecycleOwner, EventObserver {
            openMovieDetail(it)
        })
    }

    private fun openPeopleDetail(id: String) {
        val action = MovieDetailFragmentDirections.actionNavMovieDetailToNavPeopleDetail(id)
        findNavController().navigate(action)
    }

    private fun openMovieDetail(id: String) {
        val action = MovieDetailFragmentDirections.actionNavMovieDetailToSelf(id)
        findNavController().navigate(action)
    }

    private fun subscribeUi() {
        viewModel.cast.observe(this.viewLifecycleOwner, Observer {
            castAdapter.submitList(it)
        })
    }
}