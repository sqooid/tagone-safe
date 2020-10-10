package com.example.tagone

import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
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

        setupDestinationChangeListener()
    }

    // Back button navigation
    override fun onSupportNavigateUp() =
        navigateUp(findNavController(R.id.nav_host_fragment), binding.drawerLayout)

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
     * NavController destination change listener
     */
    private fun setupDestinationChangeListener() {
        val navController = findNavController(R.id.nav_host_fragment)
        navController.addOnDestinationChangedListener { _, dest, _ ->
            val toolBar = findViewById<Toolbar>(R.id.toolbar)
            val appBar = findViewById<AppBarLayout>(R.id.appbar_layout)
            val toolBarParams = toolBar.layoutParams as AppBarLayout.LayoutParams
            when (dest.id) {
                R.id.detailedViewFragment -> {
                    appBar.setExpanded(false, false)
                    toolBarParams.scrollFlags =
                        AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED

                }
                R.id.tag_search -> {
                    toolBarParams.scrollFlags =
                        AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
                }
            }
        }
    }
}

