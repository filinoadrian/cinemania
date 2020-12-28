package com.far_sstrwnt.cinemania.ui.home

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.far_sstrwnt.cinemania.R
import com.far_sstrwnt.cinemania.databinding.FragmentHomePagerBinding
import com.far_sstrwnt.cinemania.model.GenreEntity
import com.far_sstrwnt.cinemania.model.MediaType
import com.far_sstrwnt.cinemania.ui.EntityLoadStateAdapter
import com.far_sstrwnt.cinemania.ui.MediaAdapter
import com.far_sstrwnt.cinemania.ui.MediaListAdapter
import com.far_sstrwnt.cinemania.ui.MediaPagingAdapter
import com.google.android.material.chip.Chip
import com.google.android.material.tabs.TabLayoutMediator
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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
        viewDataBinding = FragmentHomePagerBinding.inflate(inflater, container, false)

        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initAdapter()
        initPagingAdapter()

        viewModel.fetchMediaTrending(mediaType.value)
        viewModel.fetchMediaGenre(mediaType.value)

        viewModel.mediaTrending.observe(this.viewLifecycleOwner, {
            mediaAdapter.setShowList(it)
        })

        viewModel.mediaGenre.observe(this.viewLifecycleOwner, {
            initGenre(it)
        })
    }

    private fun initGenre(genreList: List<GenreEntity>) {
        val chipGroup = viewDataBinding.genreList
        val inflater = LayoutInflater.from(chipGroup.context)

        val children = genreList.map { genre ->
            val chip = inflater.inflate(R.layout.item_genre, chipGroup, false) as Chip
            chip.text = genre.name
            chip.tag = genre.name

            chip
        }

        chipGroup.removeAllViews()

        for (chip in children) {
            chipGroup.addView(chip)
        }
    }

    private fun initPagingAdapter() {
        mediaListAdapter = MediaListAdapter(viewModel)
        mediaListAdapter.submitList(mediaList)

        viewDataBinding.mediaList.adapter = mediaListAdapter
    }

    private fun initAdapter() {
        mediaAdapter = MediaAdapter()
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