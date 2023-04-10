package com.dolla.yumyum.ui.activites

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.dolla.yumyum.adapters.CategoryMealsAdapter
import com.dolla.yumyum.data.pojo.MealsByCategory
import com.dolla.yumyum.databinding.ActivityCategoryMealsBinding
import com.dolla.yumyum.ui.fragments.HomeFragment.Companion.CATEGORY_NAME
import com.dolla.yumyum.ui.fragments.HomeFragment.Companion.MEAL_ID
import com.dolla.yumyum.ui.fragments.HomeFragment.Companion.MEAL_NAME
import com.dolla.yumyum.ui.fragments.HomeFragment.Companion.MEAL_THUMB
import com.dolla.yumyum.viewModel.CategoryMealsViewModel

class CategoryMealsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryMealsBinding
    private lateinit var categoryMealsViewModel: CategoryMealsViewModel
    private lateinit var categoryMealsAdapter: CategoryMealsAdapter
    private lateinit var categoryName: String

    override fun onCreate(savedInstanceState: Bundle?) { // Called when the activity is first created.
        super.onCreate(savedInstanceState)
        // Initialize the binding object instance using the layout file name
        binding = ActivityCategoryMealsBinding.inflate(layoutInflater)
        // Set the content view to the binding object's root
        setContentView(binding.root)

        // Initialize the categoryMealsViewModel
        categoryMealsViewModel = ViewModelProvider(this)[CategoryMealsViewModel::class.java]

        // Initialize the favouritesAdapter object instance
        categoryMealsAdapter = CategoryMealsAdapter()
    }

    override fun onStart() { // Called when the activity is about to become visible.
        super.onStart()
        prepareCategoryMealsRecyclerView() // Prepare the RecyclerView for the category meals
        getCategoryNameFromIntent() // Get the category name from the intent
        categoryMealsViewModel.getMealsByCategory(categoryName) // Call the getMealsByCategory() method of the categoryMealsViewModel (fire the API call)
        observeCategoryMeals() // Observe the category meals
        onCategoryMealClick() // Set the click listener on the category meal item
    }

    private fun prepareCategoryMealsRecyclerView() { // Prepare the RecyclerView for the category meals
        binding.rvCategoryMeals.apply {  // Apply the following code to the binding object's rvCategoryMeals
            layoutManager = GridLayoutManager(
                this@CategoryMealsActivity,
                2,
                GridLayoutManager.VERTICAL,
                false
            ) // Set the layout manager to a GridLayoutManager
            adapter = categoryMealsAdapter // Set the adapter to the categoryMealsAdapter
            setHasFixedSize(true) // Set the hasFixedSize property to true (to improve performance
        }
    }

    private fun getCategoryNameFromIntent() { // Get the category name from the intent
        val intent = intent // getIntent() method
        intent.getStringExtra(CATEGORY_NAME)
            ?.let { categoryName = it } // Get the category name from the intent
    }

    private fun observeCategoryMeals() { // Observe the categoryMealsViewModel's categoryMeals LiveData
        categoryMealsViewModel.categoryMealsLiveData.observe(this) { categoryMeals ->
            binding.tvCategoryMealsCount.text = categoryName
            categoryMeals?.let {
                binding.progressBarCategoryMeals.visibility = GONE // Hide the progress bar
                val mealCountText = "${categoryName}: ${categoryMeals.meals.size}"
                binding.tvCategoryMealsCount.text =
                    mealCountText // Set the text of the binding object's tvCategoryMealsCount to the mealCountText
                val categoryMealsList =
                    ArrayList<MealsByCategory>() // Initialize the categoryMealsList
                categoryMeals.meals.let { categoryMealsList.addAll(it) } // Add the categoryMeals to the categoryMealsList
                categoryMealsAdapter.setCategoryMealsList(categoryMealsList) // Set the categoryMealsList of the categoryMealsAdapter
            }
        }
    }

    private fun onCategoryMealClick() { // Set the click listener on the category meal item
        categoryMealsAdapter.onCategoryMealClicked = { meal ->
            val intent = Intent(
                this@CategoryMealsActivity,
                MealActivity::class.java
            ) // Create an intent to start the MealActivity
            intent.apply {
                putExtra(MEAL_ID, meal.id) // Put the meal ID in the intent extras
                putExtra(MEAL_NAME, meal.name) // Put the meal name in the intent extras
                putExtra(MEAL_THUMB, meal.thumbUrl) // Put the meal thumb URL in the intent extras
            }
            startActivity(intent) // Start the MealActivity
        }
    }
}