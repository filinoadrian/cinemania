package com.far_sstrwnt.cinemania.ui

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.far_sstrwnt.cinemania.R
import com.far_sstrwnt.cinemania.databinding.ActivityMainBinding
import com.far_sstrwnt.cinemania.util.contentView
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity() {

    private val binding: ActivityMainBinding by contentView(R.layout.activity_main)

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            navController = Navigation.findNavController(this@MainActivity, R.id.nav_host_fragment)
            bottomNav.setupWithNavController(navController)
        }
    }
}
