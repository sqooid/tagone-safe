package com.example.tagone

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.tagone.databinding.ActivityMainBinding
import com.google.android.material.appbar.AppBarLayout

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // onCreate method for main activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setupToolbar()

        setupDesinationChangeListener()
    }

    // Back button navigation
    override fun onSupportNavigateUp() = navigateUp(findNavController(R.id.nav_host_fragment), binding.drawerLayout)

    // Toolbar nav drawer setup
    private fun setupToolbar() {
        // Nav controller
        val navController = findNavController(R.id.nav_host_fragment)

        setSupportActionBar(binding.toolbar)

        // Setting up action bar
        setupActionBarWithNavController(navController, binding.drawerLayout)

        // Setting up left navigation drawer
        binding.navigationView.setupWithNavController(navController)
    }

    /**
     * Navcontroller destination change listener
     */
    private fun setupDesinationChangeListener() {
        val navcontroller = findNavController(R.id.nav_host_fragment)
        navcontroller.addOnDestinationChangedListener { _, _, _ ->
            findViewById<AppBarLayout>(R.id.appbar_layout).setExpanded(true)
        }
    }
}

