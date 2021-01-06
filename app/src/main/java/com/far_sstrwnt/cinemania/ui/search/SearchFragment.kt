package com.far_sstrwnt.cinemania.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.far_sstrwnt.cinemania.databinding.FragmentSearchBinding
import com.far_sstrwnt.cinemania.model.MediaType
import com.far_sstrwnt.cinemania.shared.result.EventObserver
import com.google.android.material.tabs.TabLayoutMediator
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SearchFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<SearchViewModel> { viewModelFactory }

    private lateinit var viewDataBinding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = FragmentSearchBinding.inflate(inflater, container, false)

        return viewDataBinding.root
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
        appCompatActivity.setSupportActionBar(viewDataBinding.toolbar)
    }

    private fun initAdapter() {
        viewDataBinding.viewPager.adapter = SearchFragmentStateAdapter(childFragmentManager, lifecycle)
        TabLayoutMediator(viewDataBinding.tabLayout, viewDataBinding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Movies"
                1 -> tab.text = "Series"
            }
        }.attach()
    }

    private fun initSearch() {
        viewDataBinding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isNotEmpty()) {
                    viewModel.setQueryValue(query)
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
        viewModel.navigateToMediaDetailAction.observe(this.viewLifecycleOwner, EventObserver {
            findNavController().navigate(SearchFragmentDirections.actionNavSearchToNavMediaDetail(it.first, it.second))
        })
    }

    private inner class SearchFragmentStateAdapter(fm: FragmentManager?, lifecycle: Lifecycle)
        : FragmentStateAdapter(fm!!, lifecycle) {

        override fun createFragment(position: Int): Fragment {
            var fragment: Fragment? = null
            when (position) {
                0 -> fragment = SearchPagerFragment(MediaType.MOVIE, viewModel)
                1 -> fragment = SearchPagerFragment(MediaType.TV, viewModel)
            }
            return fragment!!
        }

        override fun getItemCount(): Int {
            return 2
        }
    }
}