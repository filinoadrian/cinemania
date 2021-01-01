package com.far_sstrwnt.cinemania.ui.mediadetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.far_sstrwnt.cinemania.R
import com.far_sstrwnt.cinemania.databinding.FragmentMediaDetailBinding
import com.far_sstrwnt.cinemania.model.MediaType
import com.far_sstrwnt.cinemania.model.VideoEntity
import com.far_sstrwnt.cinemania.shared.result.EventObserver
import com.far_sstrwnt.cinemania.ui.adapter.*
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

    private lateinit var episodeAdapter: EpisodeAdapter

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
        initEpisodeAdapter()
        initCastAdapter()
        initVideoAdapter()
        initPagingAdapter()
        initNavigation()

        viewModel.fetchMediaFavoriteById(args.id)
        viewModel.fetchMediaDetail(args.mediaType, args.id)
        viewModel.fetchMediaCast(args.mediaType, args.id)
        viewModel.fetchMediaVideos(args.mediaType, args.id)
        fetchMediaSimilar(args.mediaType, args.id)

        if (args.mediaType == MediaType.TV.value) {
            viewModel.fetchMediaEpisodes(args.id, 1)
        }

        viewModel.isFavorite.observe(this.viewLifecycleOwner, { isFavorite ->
            if (isFavorite) {
                viewDataBinding.favoriteButton.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.color_secondary),
                    android.graphics.PorterDuff.Mode.SRC_IN)
            } else {
                viewDataBinding.favoriteButton.setColorFilter(
                    ContextCompat.getColor(requireContext(), android.R.color.darker_gray),
                    android.graphics.PorterDuff.Mode.SRC_IN)
            }
        })

        viewModel.mediaDetail.observe(this.viewLifecycleOwner, { mediaDetail ->
            mediaDetail.numberOfSeasons?.let {
                initSpinner(it)
            }
        })
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

    private fun initSpinner(numberOfSeasons: Int) {
        val seasonChoices = mutableListOf<String>()

        for (i in 1..numberOfSeasons) {
            seasonChoices.add("Season $i")
        }

        val spinnerArrayAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            seasonChoices
        )

        viewDataBinding.mediaSeasonSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.fetchMediaEpisodes(args.id, position + 1)
            }
        }

        viewDataBinding.mediaSeasonSpinner.adapter = spinnerArrayAdapter;
        viewDataBinding.mediaSeasonSpinner.setSelection(0)
    }

    private fun initEpisodeAdapter() {
        episodeAdapter = EpisodeAdapter()
        viewDataBinding.mediaEpisodeList.adapter = episodeAdapter
        viewDataBinding.mediaEpisodeList.addItemDecoration(ItemDecoration(resources.getDimensionPixelSize(R.dimen.padding_normal)))

        viewModel.mediaEpisodes.observe(this.viewLifecycleOwner, {
            episodeAdapter.submitList(it)
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