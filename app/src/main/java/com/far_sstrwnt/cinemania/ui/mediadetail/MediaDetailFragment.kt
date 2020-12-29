package com.far_sstrwnt.cinemania.ui.mediadetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.far_sstrwnt.cinemania.R
import com.far_sstrwnt.cinemania.databinding.FragmentMediaDetailBinding
import com.far_sstrwnt.cinemania.model.VideoEntity
import com.far_sstrwnt.cinemania.shared.result.EventObserver
import com.far_sstrwnt.cinemania.ui.adapter.CastAdapter
import com.far_sstrwnt.cinemania.ui.adapter.EntityLoadStateAdapter
import com.far_sstrwnt.cinemania.ui.adapter.MediaPagingAdapter
import com.far_sstrwnt.cinemania.ui.adapter.VideoAdapter
import com.far_sstrwnt.cinemania.ui.common.VideoActionsHandler
import com.far_sstrwnt.cinemania.util.openWebsiteUrl
import com.google.android.material.appbar.AppBarLayout
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class MediaDetailFragment : DaggerFragment(), VideoActionsHandler {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<MediaDetailViewModel> { viewModelFactory }

    private lateinit var viewDataBinding: FragmentMediaDetailBinding

    private lateinit var castAdapter: CastAdapter

    private lateinit var videoAdapter: VideoAdapter

    private lateinit var similarAdapter: MediaPagingAdapter

    private val args: MediaDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = FragmentMediaDetailBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }

        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner

        initAppBar()
        initCastAdapter()
        initVideoAdapter()
        initPagingAdapter()
        initNavigation()

        viewModel.fetchMediaDetail(args.mediaType, args.id)
        viewModel.fetchMediaCast(args.mediaType, args.id)
        viewModel.fetchMediaVideos(args.mediaType, args.id)
        fetchMediaSimilar(args.mediaType, args.id)
    }

    private fun initAppBar() {
        val appCompatActivity = activity as AppCompatActivity
        appCompatActivity.setSupportActionBar(viewDataBinding.toolbar)
        appCompatActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var isShow = true
        var scrollRange = -1
        viewDataBinding.appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (scrollRange == -1) {
                scrollRange = appBarLayout?.totalScrollRange!!
            }
            if (scrollRange + verticalOffset == 0){
                viewDataBinding.collapsingToolbar.title = viewModel.mediaDetail.value?.name
                isShow = true
            } else if (isShow){
                viewDataBinding.collapsingToolbar.title = " "
                isShow = false
            }
        })
    }

    private fun initCastAdapter() {
        castAdapter = CastAdapter()
        viewDataBinding.mediaCastList.adapter = castAdapter

        viewModel.mediaCast.observe(this.viewLifecycleOwner, {
            castAdapter.submitList(it)
        })
    }

    private fun initVideoAdapter() {
        videoAdapter = VideoAdapter(this)
        viewDataBinding.mediaVideoList.adapter = videoAdapter

        viewModel.mediaVideos.observe(this.viewLifecycleOwner, {
            videoAdapter.submitList(it)
        })
    }

    private fun initPagingAdapter() {
        similarAdapter = MediaPagingAdapter(
            args.mediaType,
            viewModel,
            R.layout.item_media
        )
        viewDataBinding.mediaSimilarList.adapter = similarAdapter.withLoadStateHeaderAndFooter(
            header = EntityLoadStateAdapter { similarAdapter.retry() },
            footer = EntityLoadStateAdapter { similarAdapter.retry() }
        )
    }

    private fun initNavigation() {
        viewModel.navigateToMediaDetailAction.observe(this.viewLifecycleOwner, EventObserver {
            findNavController().navigate(MediaDetailFragmentDirections.actionNavMediaDetailSelf(it.first, it.second))
        })
    }

    private fun fetchMediaSimilar(mediaType: String, id: String) {
        viewModel.viewModelScope.launch {
            viewModel.fetchMediaSimilar(mediaType, id).collectLatest {
                similarAdapter.submitData(it)
            }
        }
    }

    override fun playVideo(video: VideoEntity) {
        openWebsiteUrl(requireContext(), "https://www.youtube.com/watch?v=${video.key}")
    }
}