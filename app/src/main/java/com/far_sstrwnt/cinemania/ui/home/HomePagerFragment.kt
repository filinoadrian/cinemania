package com.far_sstrwnt.cinemania.ui.home

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.far_sstrwnt.cinemania.R
import com.far_sstrwnt.cinemania.databinding.FragmentHomePagerBinding
import com.far_sstrwnt.cinemania.model.GenreEntity
import com.far_sstrwnt.cinemania.model.MediaType
import com.far_sstrwnt.cinemania.shared.result.EventObserver
import com.far_sstrwnt.cinemania.ui.adapter.MediaAdapter
import com.far_sstrwnt.cinemania.ui.adapter.MediaListAdapter
import com.far_sstrwnt.cinemania.util.setupSnackbar
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.android.support.DaggerFragment
import javax.inject.Inject
import kotlin.math.abs


class HomePagerFragment(
    private val mediaType: MediaType,
    private val mediaList: List<MediaList>
) : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<HomeViewModel> { viewModelFactory }

    private lateinit var viewDataBinding: FragmentHomePagerBinding

    private lateinit var mediaAdapter: MediaAdapter

    private lateinit var mediaListAdapter: MediaListAdapter

    private var sliderHandler = Handler()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = FragmentHomePagerBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }

        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner

        initAdapter()
        initPagingAdapter()
        initSnackbar()
        initNavigation()

        viewModel.fetchMediaTrending(mediaType.value)

        viewModel.mediaTrending.observe(this.viewLifecycleOwner, {
            mediaAdapter.setMediaList(it)
        })
    }

    private fun initPagingAdapter() {
        mediaListAdapter = MediaListAdapter(mediaType.value, viewModel)
        mediaListAdapter.submitList(mediaList)

        viewDataBinding.mediaList.adapter = mediaListAdapter
    }

    private fun initNavigation() {
        viewModel.navigateToMediaDetailAction.observe(this.viewLifecycleOwner, EventObserver {
            findNavController().navigate(HomeFragmentDirections.actionNavHomeToNavMediaDetail(it.first, it.second))
        })
    }

    private fun initSnackbar() {
        view?.setupSnackbar(this, viewModel.snackbarMessage, Snackbar.LENGTH_SHORT)
    }

    private fun initAdapter() {
        mediaAdapter = MediaAdapter(mediaType.value, viewModel, R.layout.item_media_trending)
        viewDataBinding.viewPager.adapter = mediaAdapter
        viewDataBinding.viewPager.offscreenPageLimit = 3

        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(0))
        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.15f
        }

        viewDataBinding.viewPager.setPageTransformer(compositePageTransformer)

        viewDataBinding.viewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                sliderHandler.removeCallbacks(sliderRunnable)
                sliderHandler.postDelayed(sliderRunnable, 3000)
            }
        })

        TabLayoutMediator(viewDataBinding.tabLayout, viewDataBinding.viewPager) { tab, position ->
            viewDataBinding.viewPager.setCurrentItem(tab.position, true)
        }.attach()
    }

    private val sliderRunnable = Runnable {
        if (viewDataBinding.viewPager.currentItem == mediaAdapter.itemCount - 1) {
            viewDataBinding.viewPager.currentItem = 0
        } else {
            viewDataBinding.viewPager.currentItem = viewDataBinding.viewPager.currentItem + 1
        }
    }

    override fun onPause() {
        super.onPause()
        sliderHandler.removeCallbacks(sliderRunnable)
    }

    override fun onResume() {
        super.onResume()
        sliderHandler.postDelayed(sliderRunnable, 3000)
    }
}