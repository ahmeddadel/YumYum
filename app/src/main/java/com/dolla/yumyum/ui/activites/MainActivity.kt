package com.dolla.yumyum.ui.activites

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.dolla.yumyum.R
import com.dolla.yumyum.data.db.MealDatabase
import com.dolla.yumyum.databinding.ActivityMainBinding
import com.dolla.yumyum.viewModel.HomeViewModel
import com.dolla.yumyum.viewModel.HomeViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    val viewModel: HomeViewModel by lazy {  // Lazy initialization means that the value will be initialized only when it is used for the first time.
        val mealDatabase = MealDatabase.getInstance(this) // Get the instance of the MealDatabase
        val homeViewModelFactory =
            HomeViewModelFactory(mealDatabase) // Create the HomeViewModelFactory

        ViewModelProvider(
            this,
            homeViewModelFactory
        )[HomeViewModel::class.java] // Create the HomeViewModel using the HomeViewModelFactory
    } // ViewModel object instance to be used in other fragments

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