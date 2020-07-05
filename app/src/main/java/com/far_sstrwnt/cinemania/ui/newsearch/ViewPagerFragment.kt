package com.far_sstrwnt.cinemania.ui.newsearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.far_sstrwnt.cinemania.databinding.FragmentViewPagerBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class ViewPagerFragment(private val viewModel: NewSearchViewModel) : Fragment() {

    private lateinit var binding: FragmentViewPagerBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentViewPagerBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.lifecycleOwner = this.viewLifecycleOwner

        viewModel.currentQueryValue.observe(this.viewLifecycleOwner, Observer {
            binding.fragmentText.text = it
        })
    }
}