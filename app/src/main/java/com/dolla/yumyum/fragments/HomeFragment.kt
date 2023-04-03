package com.dolla.yumyum.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.dolla.yumyum.activites.CategoryMealsActivity
import com.dolla.yumyum.activites.MealActivity
import com.dolla.yumyum.adapters.CategoriesAdapter
import com.dolla.yumyum.adapters.PopularMealsAdapter
import com.dolla.yumyum.databinding.FragmentHomeBinding
import com.dolla.yumyum.pojo.Category
import com.dolla.yumyum.pojo.Meal
import com.dolla.yumyum.pojo.PopularMeal
import com.dolla.yumyum.viewModel.HomeViewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding // View binding
    private lateinit var homeViewModel: HomeViewModel // ViewModel
    private lateinit var randomMeal: Meal // Meal object
    private lateinit var popularMealsAdapter: PopularMealsAdapter // Adapter for the RecyclerView in the HomeFragment
    private lateinit var categoriesAdapter: CategoriesAdapter // Adapter for the RecyclerView in the HomeFragment

    // Companion object (static) to store the keys for the intent extras (used in the MealActivity)
    companion object {
        const val MEAL_ID = "com.dolla.yumyum.fragments.MEAL_ID"
        const val MEAL_NAME = "com.dolla.yumyum.fragments.MEAL_NAME"
        const val MEAL_THUMB = "com.dolla.yumyum.fragments.MEAL_THUMB"
        const val CATEGORY_NAME = "com.dolla.yumyum.fragments.CATEGORY_NAME"
    }

    override fun onCreate(savedInstanceState: Bundle?) { // This method is called when the fragment is first created
        super.onCreate(savedInstanceState)

        // Get an instance of the HomeViewModel class (ViewModelProvider is a factory class)
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        // Initialize the popularMealsAdapter object instance
        popularMealsAdapter = PopularMealsAdapter()

        // Initialize the categoriesAdapter object instance
        categoriesAdapter = CategoriesAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View { // This method is called to have the fragment instantiate its user interface view

        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) { // This method is called after onCreateView() when the fragment's view hierarchy is created and ready to be used
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.getRandomMeal() // Call the getRandomMeal() method of the HomeViewModel (fire the API call)
        observeRandomMeal() // Observe the randomMealLiveData of the HomeViewModel
        onRandomMealClick() // Set the onClickListener for the random meal ImageView

        preparePopularMealsRecyclerView() // Prepare the RecyclerView for the popular meals
        homeViewModel.getPopularMeals() // Call the getPopularMeals() method of the HomeViewModel (fire the API call)
        observePopularMeals() // Observe the popularMealsLiveData of the HomeViewModel
        onPopularMealClick() // Set the onClickListener for the popular meals

        prepareCategoriesRecyclerView() // Prepare the RecyclerView for the categories
        homeViewModel.getCategories() // Call the getCategories() method of the HomeViewModel (fire the API call)
        observeCategories() // Observe the categoriesLiveData of the HomeViewModel
        onCategoryClick() // Set the onClickListener for the categories
    }

    private fun observeRandomMeal() { // Observe the randomMealLiveData of the HomeViewModel
        homeViewModel.randomMealLiveData.observe(viewLifecycleOwner) { meal ->
            Glide.with(this@HomeFragment) // this@HomeFragment is the context of the fragment
                .load(meal.thumbUrl) // Load the meal image from the URL
                .into(binding.ivRandomMeal) // Set the image to the ImageView

            this.randomMeal = meal // Set the randomMeal object to the meal object
        }
    }

    private fun onRandomMealClick() { // Set the onClickListener for the random meal ImageView
        binding.ivRandomMeal.setOnClickListener { // Set the onClickListener for the random meal ImageView
            val intent = Intent(
                activity,
                MealActivity::class.java
            ) // Create an intent to start the MealActivity
            intent.putExtra(MEAL_ID, randomMeal.id) // Put the meal ID in the intent extras
            intent.putExtra(MEAL_NAME, randomMeal.name) // Put the meal name in the intent extras
            intent.putExtra(
                MEAL_THUMB,
                randomMeal.thumbUrl
            ) // Put the meal image URL in the intent extras
            startActivity(intent) // Start the MealActivity (pass the intent)
        }
    }

    private fun preparePopularMealsRecyclerView() {
        binding.rvPopularItems.apply {  // Apply the following code to the RecyclerView
            layoutManager = LinearLayoutManager(
                activity,
                LinearLayoutManager.HORIZONTAL,
                false
            ) // Set the layout manager of the RecyclerView to a LinearLayoutManager
            adapter =
                popularMealsAdapter // Set the adapter of the RecyclerView to the popularMealsAdapter of the HomeViewModel
            setHasFixedSize(true) // Set the hasFixedSize property to true (improves performance)
        }
    }

    private fun observePopularMeals() { // Observe the popularMealsLiveData of the HomeViewModel
        homeViewModel.popularMealsLiveData.observe(viewLifecycleOwner) { mealList ->
            popularMealsAdapter.setMealsList(mealList as ArrayList<PopularMeal>) // cast the mealList to an ArrayList<PopularMeal> and pass it to the setMealsList() method of the popularMealsAdapter
        }
    }

    private fun onPopularMealClick() { // Set the onClickListener for the popular meals
        popularMealsAdapter.onPopularMealClicked = { popularMeal ->
            val intent = Intent(
                activity,
                MealActivity::class.java
            ) // Create an intent to start the MealActivity
            intent.putExtra(MEAL_ID, popularMeal.id) // Put the meal ID in the intent extras
            intent.putExtra(MEAL_NAME, popularMeal.name) // Put the meal name in the intent extras
            intent.putExtra(
                MEAL_THUMB,
                popularMeal.thumbUrl
            ) // Put the meal image URL in the intent extras
            startActivity(intent) // Start the MealActivity (pass the intent)
        }
    }

    private fun prepareCategoriesRecyclerView() { // Prepare the RecyclerView for the categories
        binding.rvCategories.apply {  // Apply the following code to the RecyclerView
            layoutManager = GridLayoutManager(
                activity,
                3
            ) // Set the layout manager of the RecyclerView to a GridLayoutManager
            adapter =
                categoriesAdapter // Set the adapter of the RecyclerView to the popularMealsAdapter of the HomeViewModel
            setHasFixedSize(true) // Set the hasFixedSize property to true (improves performance)
        }
    }

    private fun observeCategories() { // Observe the categoriesLiveData of the HomeViewModel
        homeViewModel.categoriesLiveData.observe(viewLifecycleOwner) { categories ->
            categoriesAdapter.setCategoriesList(categories as ArrayList<Category>) // cast the categories to an ArrayList<Category> and pass it to the setCategoriesList() method of the categoriesAdapter
        }
    }

    private fun onCategoryClick() { // Set the onClickListener for the categories
        categoriesAdapter.onCategoryClicked = { category ->
            val intent = Intent(
                activity,
                CategoryMealsActivity::class.java
            ) // Create an intent to start the CategoryActivity
            intent.putExtra(CATEGORY_NAME, category.name) // Put the category in the intent extras
            startActivity(intent) // Start the CategoryActivity (pass the intent)
        }
    }
}