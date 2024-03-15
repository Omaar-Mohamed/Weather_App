package com.example.weatherapp


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize NavController
        navController = findNavController(R.id.nav_host_fragment)

        // Set up Toolbar as ActionBar
        setSupportActionBar(binding.toolbar)

        // Set navigation icon
        binding.toolbar.setNavigationIcon(R.drawable.icons8_menu)

        // Set up navigation drawer
        drawerLayout = binding.drawerLayout
        navigationView = binding.navView

        // Set click listener for navigation icon to open drawer
        binding.toolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(navigationView)
        }

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.homeFragment -> {
                    navController.navigate(R.id.homeFragment)
                    closeDrawer()
                }

                R.id.favouritFragment -> {
                    navController.navigate(R.id.favouritFragment)
                    closeDrawer()
                }

                R.id.alertFragment -> {
                    navController.navigate(R.id.alertFragment)
                    closeDrawer()
                }

                R.id.settingsFragment -> {
                    navController.navigate(R.id.settingsFragment)
                    closeDrawer()
                }
                // Handle clicks for additional menu items
            }
            true
        }
    }
        private fun closeDrawer() {
            drawerLayout.closeDrawer(GravityCompat.START)
        }

    }
