package com.dolla.yumyum.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.dolla.yumyum.R
import com.dolla.yumyum.adapters.CategoriesAdapter
import com.dolla.yumyum.adapters.PopularMealsAdapter
import com.dolla.yumyum.data.pojo.Category
import com.dolla.yumyum.data.pojo.Meal
import com.dolla.yumyum.data.pojo.PopularMeal
import com.dolla.yumyum.databinding.FragmentHomeBinding
import com.dolla.yumyum.ui.activites.CategoryMealsActivity
import com.dolla.yumyum.ui.activites.MainActivity
import com.dolla.yumyum.ui.activites.MealActivity
import com.dolla.yumyum.viewModel.HomeViewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding // View binding
    private lateinit var viewModel: HomeViewModel // ViewModel
    private var randomMeal: Meal? = null // Meal object
    private lateinit var popularMealsAdapter: PopularMealsAdapter // Adapter for the RecyclerView in the HomeFragment
    private lateinit var categoriesAdapter: CategoriesAdapter // Adapter for the RecyclerView in the HomeFragment

    // Companion object (static) to store the keys for the intent extras (used in the MealActivity)
    companion object {
        const val MEAL_ID = "com.dolla.yumyum.ui.fragments.MEAL_ID"
        const val MEAL_NAME = "com.dolla.yumyum.ui.fragments.MEAL_NAME"
        const val MEAL_THUMB = "com.dolla.yumyum.ui.fragments.MEAL_THUMB"
        const val CATEGORY_NAME = "com.dolla.yumyum.ui.fragments.CATEGORY_NAME"
    }

    override fun onCreate(savedInstanceState: Bundle?) { // This method is called when the fragment is first created
        super.onCreate(savedInstanceState)

        // Get an instance of the HomeViewModel class (ViewModelProvider is a factory class)
//        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        viewModel =
            (activity as MainActivity).viewModel // Get the HomeViewModel instance from the MainActivity

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
        binding = FragmentHomeBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) { // This method is called after onCreateView() when the fragment's view hierarchy is created and ready to be used
        super.onViewCreated(view, savedInstanceState)

        viewModel.getRandomMeal() // Call the getRandomMeal() method of the HomeViewModel (fire the API call)
        observeRandomMeal() // Observe the randomMealLiveData of the HomeViewModel
        onRandomMealClick() // Set the onClickListener for the random meal ImageView
        onRandomMealLongClick() // Set the onLongClickListener for the random meal ImageView

        preparePopularMealsRecyclerView() // Prepare the RecyclerView for the popular meals
        viewModel.getPopularMeals() // Call the getPopularMeals() method of the HomeViewModel (fire the API call)
        observePopularMeals() // Observe the popularMealsLiveData of the HomeViewModel
        onPopularMealClick() // Set the onClickListener for the popular meals
        onPopularMealLongClick() // Set the onLongClickListener for the popular meals

        prepareCategoriesRecyclerView() // Prepare the RecyclerView for the categories
        viewModel.getCategories() // Call the getCategories() method of the HomeViewModel (fire the API call)
        observeCategories() // Observe the categoriesLiveData of the HomeViewModel
        onCategoryClick() // Set the onClickListener for the categories

        onSearchClick() // Set the onClickListener for the search ImageView
    }

    private fun observeRandomMeal() { // Observe the randomMealLiveData of the HomeViewModel
        viewModel.randomMealLiveData.observe(viewLifecycleOwner) { meal ->
            meal?.let { // Check if the meal object is not null
                binding.progressBarRandomMeal.visibility = GONE // Hide the progress bar
                Glide.with(this@HomeFragment) // this@HomeFragment is the context of the fragment
                    .load(meal.thumbUrl) // Load the meal image from the URL
                    .into(binding.ivRandomMeal) // Set the image to the ImageView

                this.randomMeal = meal // Set the randomMeal object to the meal object
            }
        }
    }

    private fun onRandomMealClick() { // Set the onClickListener for the random meal ImageView
        binding.ivRandomMeal.setOnClickListener { // Set the onClickListener for the random meal ImageView
            val intent = Intent(
                activity,
                MealActivity::class.java
            ) // Create an intent to start the MealActivity
            intent.apply {
                putExtra(MEAL_ID, randomMeal?.id) // Put the meal ID in the intent extras
                putExtra(MEAL_NAME, randomMeal?.name) // Put the meal name in the intent extras
                putExtra(
                    MEAL_THUMB,
                    randomMeal?.thumbUrl
                ) // Put the meal image URL in the intent extras
            }
            randomMeal?.let {
                startActivity(intent) // Start the MealActivity (pass the intent)
            }
        }
    }

    private fun onRandomMealLongClick() { // Set the onLongClickListener for the random meal ImageView
        binding.ivRandomMeal.setOnLongClickListener { // Set the onLongClickListener for the random meal ImageView
            val mealBottomSheetDialogFragment =
                randomMeal?.let { mealId -> MealBottomSheetDialogFragment.newInstance(mealId.id) } // Create a new instance of the MealBottomSheetDialogFragment
            mealBottomSheetDialogFragment?.show( // Show the MealBottomSheetDialogFragment
                childFragmentManager,
                MealBottomSheetDialogFragment().javaClass.simpleName
            ) // Show the MealBottomSheetDialogFragment
            true // Return true to indicate that the long click has been consumed
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
        viewModel.popularMealsLiveData.observe(viewLifecycleOwner) { popularMeals ->
            popularMeals?.let { // Check if the meals object is not null
                binding.progressBarPopularItems.visibility = GONE // Hide the progress bar
                val mealsList = ArrayList<PopularMeal>() // Initialize the mealsList
                popularMeals.meals.let { mealsList.addAll(it) } // Add the meals to the mealsList
                popularMealsAdapter.setMealsList(mealsList) // Set the mealsList to the popularMealsAdapter
            }
        }
    }

    private fun onPopularMealClick() { // Set the onClickListener for the popular meals
        popularMealsAdapter.onPopularMealClicked = { popularMeal ->
            val intent = Intent(
                activity,
                MealActivity::class.java
            ) // Create an intent to start the MealActivity
            intent.apply {
                putExtra(MEAL_ID, popularMeal.id) // Put the meal ID in the intent extras
                putExtra(MEAL_NAME, popularMeal.name) // Put the meal name in the intent extras
                putExtra(
                    MEAL_THUMB,
                    popularMeal.thumbUrl
                ) // Put the meal image URL in the intent extras
            }
            startActivity(intent) // Start the MealActivity (pass the intent)
        }
    }

    private fun onPopularMealLongClick() { // Set the onLongClickListener for the popular meals
        popularMealsAdapter.onPopularMealLongClicked = { popularMeal ->
            val mealBottomSheetDialogFragment =
                MealBottomSheetDialogFragment.newInstance(popularMeal.id) // Create a new instance of the MealBottomSheetDialogFragment
            mealBottomSheetDialogFragment.show(
                childFragmentManager,
                MealBottomSheetDialogFragment().javaClass.simpleName
            ) // Show the MealBottomSheetDialogFragment
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
        viewModel.categoriesLiveData.observe(viewLifecycleOwner) { categories ->
            categories?.let { // Check if the categories object is not null
                binding.progressBarCategories.visibility = GONE // Hide the progress bar
                binding.cardCategories.visibility = VISIBLE // Show the RecyclerView
                val categoriesList = ArrayList<Category>() // Initialize the categoriesList
                categories.categories.let { categoriesList.addAll(it) } // Add the categories to the categoriesList
                categoriesAdapter.setCategoriesList(categoriesList) // Set the categoriesList to the categoriesAdapter
            }
        }
    }

    private fun onCategoryClick() { // Set the onClickListener for the categories
        categoriesAdapter.onCategoryClicked = { category ->
            val intent = Intent(
                activity,
                CategoryMealsActivity::class.java
            ) // Create an intent to start the CategoryActivity
            intent.putExtra(
                CATEGORY_NAME,
                category.name
            ) // Put the category in the intent extras
            startActivity(intent) // Start the CategoryActivity (pass the intent)
        }
    }

    private fun onSearchClick() { // Set the onClickListener for the search ImageView
        binding.ivSearchButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment) // Navigate to the SearchFragment
        }
    }
}