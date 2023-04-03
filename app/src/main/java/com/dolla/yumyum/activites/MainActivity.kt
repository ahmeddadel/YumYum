package com.dolla.yumyum.activites

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.dolla.yumyum.R
import com.dolla.yumyum.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) { // Called when the activity is first created.
        super.onCreate(savedInstanceState)
        // Initialize the binding object instance using the layout file name
        binding = ActivityMainBinding.inflate(layoutInflater)
        // Set the content view to the binding object's root
        setContentView(binding.root)
    }

    override fun onStart() { // Called when the activity is about to become visible.
        super.onStart()
        val bottomNavigation = binding.bottomNavigation // BottomNavigationView object
        val navController = Navigation.findNavController(
            this,
            R.id.nav_host_fragment
        ) // NavController object

        // Setup the bottom navigation view with the nav controller object
        NavigationUI.setupWithNavController(bottomNavigation, navController)
    }
}