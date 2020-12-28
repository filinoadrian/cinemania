package com.far_sstrwnt.cinemania.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.far_sstrwnt.cinemania.databinding.FragmentHomeBinding
import com.far_sstrwnt.cinemania.model.Entity
import com.far_sstrwnt.cinemania.model.MediaCategory
import com.far_sstrwnt.cinemania.model.MediaType
import com.google.android.material.tabs.TabLayoutMediator
import dagger.android.support.DaggerFragment

class HomeFragment : DaggerFragment() {

    private lateinit var viewDataBinding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = FragmentHomeBinding.inflate(inflater, container, false)

        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initAdapter()
    }

    private fun initAdapter() {
        viewDataBinding.viewPager.adapter = HomeFragmentStateAdapter(childFragmentManager, lifecycle)
        viewDataBinding.viewPager.isUserInputEnabled = false

        TabLayoutMediator(viewDataBinding.tabLayout, viewDataBinding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Movies"
                1 -> tab.text = "Series"
            }
        }.attach()
    }

    private inner class HomeFragmentStateAdapter(fm: FragmentManager?, lifecycle: Lifecycle)
        : FragmentStateAdapter(fm!!, lifecycle) {

        override fun createFragment(position: Int): Fragment {
            var fragment: Fragment? = null
            when (position) {
                0 -> fragment = HomePagerFragment(MediaType.MOVIE, movieMediaList)
                1 -> fragment = HomePagerFragment(MediaType.TV, tvMediaList)
            }
            return fragment!!
        }

        override fun getItemCount(): Int {
            return 2
        }
    }

    val movieMediaList = listOf(
        MediaList("Now Playing", MediaCategory.NOW_PLAYING, MediaType.MOVIE),
        MediaList("Upcoming", MediaCategory.UPCOMING, MediaType.MOVIE),
        MediaList("Popular", MediaCategory.POPULAR, MediaType.MOVIE),
        MediaList("Top Rated", MediaCategory.TOP_RATED, MediaType.MOVIE)
    )

    val tvMediaList = listOf(
        MediaList("Airing Today", MediaCategory.AIRING_TODAY, MediaType.TV),
        MediaList("On The Air", MediaCategory.ON_THE_AIR, MediaType.TV),
        MediaList("Popular", MediaCategory.POPULAR, MediaType.TV),
        MediaList("Top Rated", MediaCategory.TOP_RATED, MediaType.TV)
    )
}

data class MediaList(
    val title: String,
    val category: MediaCategory,
    val type: MediaType
)