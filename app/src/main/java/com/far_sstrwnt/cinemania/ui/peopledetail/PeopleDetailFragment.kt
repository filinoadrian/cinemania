package com.far_sstrwnt.cinemania.ui.peopledetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.far_sstrwnt.cinemania.databinding.FragmentPeopleDetailBinding
import com.far_sstrwnt.cinemania.shared.result.EventObserver
import com.far_sstrwnt.cinemania.ui.CastAdapter
import com.far_sstrwnt.cinemania.ui.MovieAdapter
import com.far_sstrwnt.cinemania.util.viewModelProvider
import com.google.android.material.appbar.AppBarLayout
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class PeopleDetailFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: PeopleDetailViewModel

    private lateinit var binding: FragmentPeopleDetailBinding

    private lateinit var movieAdapter: MovieAdapter

    private val args: PeopleDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = viewModelProvider(viewModelFactory)

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

        viewModel.loadPeopleDetail(args.id)
        viewModel.loadPeopleMovieCredit(args.id)

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
        binding.peopleCast.adapter = movieAdapter
    }

    private fun initNavigation() {
        viewModel.navigateToMovieDetailAction.observe(this.viewLifecycleOwner, EventObserver {
            openMovieDetail(it)
        })
    }

    private fun openMovieDetail(id: String) {
        val action = PeopleDetailFragmentDirections.actionNavPeopleDetailToNavMovieDetail(id)
        findNavController().navigate(action)
    }

    private fun subscribeUi() {
        viewModel.movies.observe(this.viewLifecycleOwner, Observer {
            movieAdapter.submitList(it)
        })
    }
}