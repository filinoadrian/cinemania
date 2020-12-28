package com.far_sstrwnt.cinemania.ui.peopledetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.far_sstrwnt.cinemania.databinding.FragmentPeopleDetailBinding
import com.far_sstrwnt.cinemania.shared.result.EventObserver
import com.far_sstrwnt.cinemania.ui.CastAdapter
import com.far_sstrwnt.cinemania.ui.MovieAdapter
import com.far_sstrwnt.cinemania.ui.TvAdapter
import com.far_sstrwnt.cinemania.util.viewModelProvider
import com.google.android.material.appbar.AppBarLayout
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class PeopleDetailFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<PeopleDetailViewModel> { viewModelFactory }

    private lateinit var binding: FragmentPeopleDetailBinding

    private lateinit var movieAdapter: MovieAdapter

    private lateinit var tvAdapter: TvAdapter

    private val args: PeopleDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPeopleDetailBinding.inflate(inflater, container, false).apply {
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
        viewModel.fetchMovieCredit(args.id)
        viewModel.fetchTvCredit(args.id)

        subscribeUi()
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
                binding.collapsingToolbar.title = viewModel.people.value?.name
                isShow = true
            } else if (isShow){
                binding.collapsingToolbar.title = " "
                isShow = false
            }
        })
    }

    private fun initAdapter() {
        movieAdapter = MovieAdapter(viewModel)
        binding.peopleMovies.adapter = movieAdapter

        tvAdapter = TvAdapter(viewModel)
        binding.peopleTv.adapter = tvAdapter
    }

    private fun initNavigation() {
        viewModel.navigateToMovieDetailAction.observe(this.viewLifecycleOwner, EventObserver {
            openMovieDetail(it)
        })
        viewModel.navigateToTvDetailAction.observe(this.viewLifecycleOwner, EventObserver {
            openTvDetail(it)
        })
    }

    private fun openMovieDetail(id: String) {
        val action = PeopleDetailFragmentDirections.actionNavPeopleDetailToNavMovieDetail(id)
        findNavController().navigate(action)
    }

    private fun openTvDetail(id: String) {
        val action = PeopleDetailFragmentDirections.actionNavPeopleDetailToNavTvDetail(id)
        findNavController().navigate(action)
    }

    private fun subscribeUi() {
        viewModel.movies.observe(this.viewLifecycleOwner, Observer {
            movieAdapter.submitList(it)
        })
        viewModel.tv.observe(this.viewLifecycleOwner, Observer {
            tvAdapter.submitList(it)
        })
    }
}