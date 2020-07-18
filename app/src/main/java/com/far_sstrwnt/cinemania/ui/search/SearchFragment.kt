package com.far_sstrwnt.cinemania.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.far_sstrwnt.cinemania.databinding.FragmentSearchBinding
import com.far_sstrwnt.cinemania.model.Entity
import com.far_sstrwnt.cinemania.shared.result.EventObserver
import com.far_sstrwnt.cinemania.util.viewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SearchFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: SearchViewModel

    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = viewModelProvider(viewModelFactory)

        binding = FragmentSearchBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initAppBar()
        initAdapter()
        initSearch()
        initNavigation()
    }

    private fun initAppBar() {
        val appCompatActivity = activity as AppCompatActivity
        appCompatActivity.setSupportActionBar(binding.toolbar)
        appCompatActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initAdapter() {
        binding.viewPager.adapter = MyAdapter(childFragmentManager, lifecycle)
        TabLayoutMediator(binding.tabLayout, binding.viewPager,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                when (position) {
                    0 -> tab.text = "Movie"
                    1 -> tab.text = "Tv Show"
                    2 -> tab.text = "People"
                }
        }).attach()
    }

    private fun initSearch() {

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isNotEmpty()) {
                    viewModel.search(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!!.isNotEmpty()) {
                    //
                } else {
                    //
                }
                return false;
            }
        })
    }

    private fun initNavigation() {
        viewModel.navigateToMovieDetailAction.observe(this.viewLifecycleOwner, EventObserver {
            openDetail(Entity.MOVIE, it)
        })
        viewModel.navigateToTvDetailAction.observe(this.viewLifecycleOwner, EventObserver {
            openDetail(Entity.TV, it)
        })
        viewModel.navigateToPeopleDetailAction.observe(this.viewLifecycleOwner, EventObserver {
            openDetail(Entity.PEOPLE, it)
        })
    }

    private fun openDetail(entity: Entity, id: String) {
        val action = when (entity) {
            Entity.MOVIE -> {
                SearchFragmentDirections.actionNavSearchToNavMovieDetail(
                    id
                )
            }
            Entity.TV -> {
                SearchFragmentDirections.actionNavSearchToNavTvDetail(
                    id
                )
            }
            Entity.PEOPLE -> {
                SearchFragmentDirections.actionNavSearchToNavPeopleDetail(
                    id
                )
            }
        }
        findNavController().navigate(action)
    }

    private inner class MyAdapter(fm: FragmentManager?, lifecycle: Lifecycle) : FragmentStateAdapter(fm!!, lifecycle) {
        private val intItems = 3

        override fun createFragment(position: Int): Fragment {
            var fragment: Fragment? = null
            when (position) {
                0 -> fragment =
                    SearchMoviePagerFragment(
                        viewModel
                    )
                1 -> fragment =
                    SearchTvPagerFragment(
                        viewModel
                    )
                2 -> fragment =
                    SearchPeoplePagerFragment(
                        viewModel
                    )
            }
            return fragment!!
        }

        override fun getItemCount(): Int {
            return intItems
        }
    }
}