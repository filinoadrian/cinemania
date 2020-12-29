package com.far_sstrwnt.cinemania.ui.tvdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import com.far_sstrwnt.cinemania.R
import com.far_sstrwnt.cinemania.databinding.FragmentTvDetailBinding
import com.far_sstrwnt.cinemania.model.VideoEntity
import com.far_sstrwnt.cinemania.shared.result.EventObserver
import com.far_sstrwnt.cinemania.ui.CastAdapter
import com.far_sstrwnt.cinemania.ui.EntityLoadStateAdapter
import com.far_sstrwnt.cinemania.ui.VideoAdapter
import com.far_sstrwnt.cinemania.ui.common.VideoActionsHandler
import com.far_sstrwnt.cinemania.ui.toVisibility
import com.far_sstrwnt.cinemania.util.openWebsiteUrl
import com.google.android.material.appbar.AppBarLayout
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class TvDetailFragment : DaggerFragment(), VideoActionsHandler {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<TvDetailViewModel> { viewModelFactory }

    private lateinit var binding: FragmentTvDetailBinding

    private lateinit var castAdapter: CastAdapter

    private lateinit var similarAdapter: TvPagingAdapter

    private lateinit var videoAdapter: VideoAdapter

    private val args: TvDetailFragmentArgs by navArgs()

    private var tvSimilarJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTvDetailBinding.inflate(inflater, container, false).apply {
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

        viewModel.fetchDetail(args.id)
        viewModel.fetchCast(args.id)
        viewModel.fetchVideo(args.id)
        loadTvSimilar(args.id)

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
                binding.collapsingToolbar.title = viewModel.tv.value?.name
                isShow = true
            } else if (isShow){
                binding.collapsingToolbar.title = " "
                isShow = false
            }
        })
    }

    private fun initAdapter() {
        castAdapter = CastAdapter(viewModel)
        binding.tvCast.adapter = castAdapter

        videoAdapter = VideoAdapter(this)
        binding.tvVideo.adapter = videoAdapter

        similarAdapter = TvPagingAdapter(
            viewModel,
            R.layout.item_tv
        )
        binding.tvSimilarList.adapter = similarAdapter.withLoadStateHeaderAndFooter(
            header = EntityLoadStateAdapter { similarAdapter.retry() },
            footer = EntityLoadStateAdapter { similarAdapter.retry() }
        )
        similarAdapter.addLoadStateListener { loadState ->
            if (loadState.refresh !is LoadState.NotLoading) {
                binding.tvSimilarList.visibility = View.GONE
                binding.progressBar.visibility =
                    toVisibility(loadState.refresh is LoadState.Loading)
                binding.retryButton.visibility =
                    toVisibility(loadState.refresh is LoadState.Error)
            } else {
                binding.tvSimilarList.visibility = View.VISIBLE
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

    private fun loadTvSimilar(id: String) {
        tvSimilarJob?.cancel()
        tvSimilarJob = lifecycleScope.launch {
            viewModel.fetchSimilar(id).collectLatest {
                similarAdapter.submitData(it)
            }
        }
    }

    private fun initNavigation() {
        viewModel.navigateToPeopleDetailAction.observe(this.viewLifecycleOwner, EventObserver {
            openPeopleDetail(it)
        })
        viewModel.navigateToTvDetailAction.observe(this.viewLifecycleOwner, EventObserver {
            openTvDetail(it)
        })
    }

    private fun openPeopleDetail(id: String) {
        val action = TvDetailFragmentDirections.actionNavTvDetailToNavPeopleDetail(id)
        findNavController().navigate(action)
    }

    private fun openTvDetail(id: String) {
        val action = TvDetailFragmentDirections.actionNavTvDetailToSelf(id)
        findNavController().navigate(action)
    }

    private fun subscribeUi() {
        viewModel.cast.observe(this.viewLifecycleOwner, Observer {
            castAdapter.submitList(it)
        })
        viewModel.video.observe(this.viewLifecycleOwner, Observer {
            videoAdapter.submitList(it)
        })
    }

    override fun playVideo(video: VideoEntity) {
        openWebsiteUrl(requireContext(), "https://www.youtube.com/watch?v=${video.key}")
    }
}
