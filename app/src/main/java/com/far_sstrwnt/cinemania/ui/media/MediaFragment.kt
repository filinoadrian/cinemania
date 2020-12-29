package com.far_sstrwnt.cinemania.ui.media

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import com.far_sstrwnt.cinemania.R
import com.far_sstrwnt.cinemania.databinding.FragmentMediaBinding
import com.far_sstrwnt.cinemania.model.GenreEntity
import com.far_sstrwnt.cinemania.model.MediaType
import com.far_sstrwnt.cinemania.shared.result.EventObserver
import com.far_sstrwnt.cinemania.ui.EntityLoadStateAdapter
import com.far_sstrwnt.cinemania.ui.GridItemDecoration
import com.far_sstrwnt.cinemania.ui.MediaPagingAdapter
import com.far_sstrwnt.cinemania.ui.toVisibility
import com.google.android.material.chip.Chip
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class MediaFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<MediaViewModel> { viewModelFactory }

    private lateinit var viewDataBinding: FragmentMediaBinding

    private lateinit var adapter: MediaPagingAdapter

    private var mediaJob: Job? = null

    private val args: MediaFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = FragmentMediaBinding.inflate(inflater, container, false)

        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initAppBar()
        initPagingAdapter()
        initNavigation()

        viewModel.fetchMediaGenre(args.mediaType)

        fetchMediaDiscover(args.mediaType, args.selectedGenre)

        viewModel.mediaGenre.observe(this.viewLifecycleOwner, {
            initGenre(args.mediaType, it)
        })

        viewDataBinding.mediaList.setHasFixedSize(true)
        viewDataBinding.mediaList.addItemDecoration(GridItemDecoration(resources.getDimensionPixelSize(R.dimen.padding_extra_small)))
        viewDataBinding.retryButton.setOnClickListener { adapter.retry() }
    }

    private fun initAppBar() {
        val appCompatActivity = activity as AppCompatActivity
        appCompatActivity.setSupportActionBar(viewDataBinding.toolbar)
        appCompatActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (args.mediaType == MediaType.MOVIE.value) {
            appCompatActivity.supportActionBar?.title = "Movies"
        } else if (args.mediaType == MediaType.TV.value) {
            appCompatActivity.supportActionBar?.title = "Series"
        }
    }

    private fun initGenre(mediaType: String, genreList: List<GenreEntity>) {
        val chipGroup = viewDataBinding.genreList
        val inflater = LayoutInflater.from(chipGroup.context)

        val children = genreList.map { genre ->
            val chip = inflater.inflate(R.layout.item_genre, chipGroup, false) as Chip
            chip.text = genre.name
            chip.tag = genre.id
            chip.isChecked = genre.id == args.selectedGenre
            chip.setOnClickListener {
                updateMediaListFromInput(mediaType, genre.id)
            }
            chip
        }

        chipGroup.removeAllViews()

        for (chip in children) {
            chipGroup.addView(chip)
        }
    }

    private fun initPagingAdapter() {
        adapter = MediaPagingAdapter(
            args.mediaType,
            viewModel,
            R.layout.item_media_grid
        )
        viewDataBinding.mediaList.adapter = adapter.withLoadStateHeaderAndFooter(
            header = EntityLoadStateAdapter { adapter.retry() },
            footer = EntityLoadStateAdapter { adapter.retry() }
        )
        adapter.addLoadStateListener { loadState ->
            if (loadState.refresh !is LoadState.NotLoading) {
                viewDataBinding.mediaList.visibility = View.GONE
                viewDataBinding.progressBar.visibility =
                    toVisibility(loadState.refresh is LoadState.Loading)
                viewDataBinding.retryButton.visibility =
                    toVisibility(loadState.refresh is LoadState.Error)
            } else {
                viewDataBinding.mediaList.visibility = View.VISIBLE
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

    private fun initNavigation() {
        viewModel.navigateToMovieDetailAction.observe(this.viewLifecycleOwner, EventObserver {
            findNavController().navigate(MediaFragmentDirections.actionNavMediaToNavMovieDetail(it))
        })

        viewModel.navigateToTvDetailAction.observe(this.viewLifecycleOwner, EventObserver {
            findNavController().navigate(MediaFragmentDirections.actionNavMediaToNavTvDetail(it))
        })
    }

    private fun fetchMediaDiscover(mediaType: String, genre: String?) {
        mediaJob?.cancel()
        mediaJob = viewModel.viewModelScope.launch {
            viewModel.fetchMediaDiscover(mediaType, genre).collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun updateMediaListFromInput(mediaType: String, genre: String) {
        viewDataBinding.genreList.checkedChipId.let {
            viewDataBinding.mediaList.scrollToPosition(0)
            fetchMediaDiscover(mediaType, genre)
        }
    }
}