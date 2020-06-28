package com.far_sstrwnt.cinemania.ui.moviedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.far_sstrwnt.cinemania.databinding.FragmentMovieDetailBinding
import com.far_sstrwnt.cinemania.ui.CastAdapter
import com.far_sstrwnt.cinemania.util.viewModelProvider
import com.google.android.material.appbar.AppBarLayout
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class MovieDetailFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: MovieDetailViewModel

    private lateinit var binding: FragmentMovieDetailBinding

    private lateinit var castAdapter: CastAdapter

    private val args: MovieDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = viewModelProvider(viewModelFactory)

        binding = FragmentMovieDetailBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.lifecycleOwner = this.viewLifecycleOwner

        initAppBar()
        initAdapter()

        viewModel.loadMovieDetail(args.id)
        viewModel.loadMovieCast(args.id)

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
                binding.collapsingToolbar.title = viewModel.movie.value?.title
                isShow = true
            } else if (isShow){
                binding.collapsingToolbar.title = " "
                isShow = false
            }
        })
    }

    private fun initAdapter() {
        castAdapter = CastAdapter()
        binding.movieCast.adapter = castAdapter
    }

    private fun subscribeUi() {
        viewModel.cast.observe(this.viewLifecycleOwner, Observer {
            castAdapter.submitList(it)
        })
    }
}