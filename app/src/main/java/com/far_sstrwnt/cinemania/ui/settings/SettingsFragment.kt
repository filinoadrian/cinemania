package com.far_sstrwnt.cinemania.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.far_sstrwnt.cinemania.MainApplication
import com.far_sstrwnt.cinemania.databinding.FragmentSettingsBinding
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class SettingsFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<SettingsViewModel> { viewModelFactory }

    private lateinit var viewDataBinding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = FragmentSettingsBinding.inflate(inflater, container, false)

        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}