package com.far_sstrwnt.cinemania.ui

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.far_sstrwnt.cinemania.R
import com.far_sstrwnt.cinemania.databinding.ActivityMainBinding
import com.far_sstrwnt.cinemania.util.hide
import com.far_sstrwnt.cinemania.util.show
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.apply {
            navController = Navigation.findNavController(this@MainActivity, R.id.nav_host_fragment)
            bottomNav.setupWithNavController(navController)

            lifecycleScope.launchWhenResumed {
                navController.addOnDestinationChangedListener { _, destination, _ ->
                    when (destination.id) {
                        R.id.nav_home, R.id.nav_search, R.id.nav_favorites -> bottomNav.show()
                        else -> bottomNav.hide()
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean = navController.navigateUp()
}
