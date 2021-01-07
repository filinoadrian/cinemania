package com.far_sstrwnt.cinemania.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.far_sstrwnt.cinemania.R
import com.far_sstrwnt.cinemania.databinding.FragmentFavoritesBinding
import com.far_sstrwnt.cinemania.model.MediaType
import com.far_sstrwnt.cinemania.shared.result.EventObserver
import com.far_sstrwnt.cinemania.ui.adapter.MediaAdapter
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class FavoriteFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<FavoriteViewModel> { viewModelFactory }

    private lateinit var viewDataBinding: FragmentFavoritesBinding

    private lateinit var adapter: MediaAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = FragmentFavoritesBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }

        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner

        initAppBar()
        initAdapter()
        initNavigation()

        viewModel.fetchMediaFavorite()

        viewModel.mediaFavorite.observe(this.viewLifecycleOwner, {
            adapter.setMediaList(it)
        })
    }

    private fun initAppBar() {
        val appCompatActivity = activity as AppCompatActivity
        appCompatActivity.setSupportActionBar(viewDataBinding.toolbar)
    }

    private fun initAdapter() {
        adapter = MediaAdapter(MediaType.MOVIE.value, viewModel, R.layout.item_media_search)
        viewDataBinding.favoriteList.adapter = adapter
    }

    private fun initNavigation() {
        viewModel.navigateToMediaDetailAction.observe(this.viewLifecycleOwner, EventObserver {
            findNavController().navigate(FavoriteFragmentDirections.actionNavFavoritesToNavMediaDetail(it.first, it.second))
        })

        viewDataBinding.findFavoriteButton.setOnClickListener {
            findNavController().navigate(FavoriteFragmentDirections.actionNavFavoritesToNavSearch())
        }
    }
}