package com.dolla.yumyum.ui.activites

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dolla.yumyum.R
import com.dolla.yumyum.data.db.MealDatabase
import com.dolla.yumyum.data.pojo.Meal
import com.dolla.yumyum.databinding.ActivityMealBinding
import com.dolla.yumyum.ui.fragments.HomeFragment.Companion.MEAL_ID
import com.dolla.yumyum.ui.fragments.HomeFragment.Companion.MEAL_NAME
import com.dolla.yumyum.ui.fragments.HomeFragment.Companion.MEAL_THUMB
import com.dolla.yumyum.viewModel.MealViewModel
import com.dolla.yumyum.viewModel.MealViewModelFactory

class MealActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMealBinding
    private lateinit var mealViewModel: MealViewModel
    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private lateinit var meal: Meal

    override fun onCreate(savedInstanceState: Bundle?) { // Called when the activity is first created.
        super.onCreate(savedInstanceState)
        // Initialize the binding object instance using the layout file name
        binding = ActivityMealBinding.inflate(layoutInflater)
        // Set the content view to the binding object's root
        setContentView(binding.root)

        // Initialize the MealViewModel
//        mealViewModel = ViewModelProvider(this)[MealViewModel::class.java]
        val mealDatabase = MealDatabase.getInstance(this) // Get the instance of the MealDatabase
        val mealViewModelFactory =
            MealViewModelFactory(mealDatabase) // Create the MealViewModelFactory
        mealViewModel = ViewModelProvider(
            this,
            mealViewModelFactory
        )[MealViewModel::class.java] // Create the MealViewModel using the MealViewModelFactory
    }

    override fun onStart() { // Called when the activity is about to become visible.
        super.onStart()
        getMealDetailsFromIntent() // Get the meal details from the intent
        setMealDetailsIntoUI() // Set the meal details into the UI
        onLoading() // Show the loading progress bar while the API call is being made
        mealViewModel.getMealById(mealId) // Get the meal details from the API using the meal ID (fire the API call)
        observeMealDetails() // Observe the mealDetailsLiveData of the MealViewModel
        onYoutubeClick() // Set the onClickListener for the YouTube button
        onFavouriteClick() // Set the onClickListener for the favourite button
    }

    private fun getMealDetailsFromIntent() { // Get the meal details from the intent
        val intent = intent // getIntent() method
        mealId = intent.getStringExtra(MEAL_ID)!! // Get the meal ID from the intent extras
        mealName = intent.getStringExtra(MEAL_NAME)!! // Get the meal name from the intent extras
        mealThumb =
            intent.getStringExtra(MEAL_THUMB)!! // Get the meal thumbnail from the intent extras

    }

    private fun setMealDetailsIntoUI() { // Set the meal details into the UI
        Glide.with(applicationContext) // application context to avoid memory leaks
            .load(mealThumb) // Load the meal thumbnail
            .into(binding.ivMealDetail) // Set the meal thumbnail into the ImageView

        binding.collapsingToolbar.title =
            mealName // Set the meal name into the CollapsingToolbarLayout
        binding.collapsingToolbar.setCollapsedTitleTextColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.white
            )
        ) // Set the collapsed title color
        binding.collapsingToolbar.statusBarScrim =
            ContextCompat.getDrawable(
                applicationContext,
                R.color.accent
            ) // Set the status bar scrim
        binding.collapsingToolbar.setExpandedTitleColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.white
            )
        ) // Set the expanded title color
    }

    private fun observeMealDetails() { // Observe the mealDetailsLiveData of the MealViewModel
        mealViewModel.mealDetailsLiveData.observe(this) { meal ->
            onSuccess() // Hide the loading progress bar when the API call is successful

            binding.tvCategory.text = meal.category // Set the meal category into the TextView
            binding.tvArea.text = meal.area // Set the meal area into the TextView
            binding.tvContent.text =
                meal.instructions // Set the meal instructions into the TextView

            isFavouriteMeal() // Check if the meal is a favourite meal to set the FAB icon

            this.meal = meal // Set the meal object to the meal object from the API call
        }
    }

    private fun isFavouriteMeal() { // Check if the meal is a favourite meal
        mealViewModel.getMealByIdFromDb(mealId).observe(this) { favouriteMeal ->
            if (favouriteMeal != null) { // If the meal is not null (it exists in the database)
                binding.fabAddToFavourites.setImageResource(R.drawable.ic_favourite) // Set the FAB icon to the favourite icon
            } else { // If the meal is null (it does not exist in the database)
                binding.fabAddToFavourites.setImageResource(R.drawable.ic_favourite_border) // Set the FAB icon to the favourite border icon
            }
        }
    }

    private fun onLoading() { // Show the loading progress bar while the API call is being made
        binding.progressBar.visibility = View.VISIBLE // Set the ProgressBar to visible
        binding.fabAddToFavourites.visibility = View.INVISIBLE // Set the FAB to invisible
        binding.tvInstructions.visibility =
            View.INVISIBLE // Set the meal instructions TextView to invisible
        binding.tvCategory.visibility =
            View.INVISIBLE // Set the meal category TextView to invisible
        binding.tvArea.visibility = View.INVISIBLE // Set the meal area TextView to invisible
        binding.ivYoutube.visibility = View.INVISIBLE // Set the YouTube ImageView to invisible
    }

    private fun onSuccess() { // Hide the loading progress bar when the API call is successful
        binding.progressBar.visibility = View.INVISIBLE // Set the ProgressBar to invisible
        binding.fabAddToFavourites.visibility = View.VISIBLE // Set the FAB to visible
        binding.tvInstructions.visibility =
            View.VISIBLE // Set the meal instructions TextView to visible
        binding.tvCategory.visibility = View.VISIBLE // Set the meal category TextView to visible
        binding.tvArea.visibility = View.VISIBLE // Set the meal area TextView to visible
        binding.ivYoutube.visibility = View.VISIBLE // Set the YouTube ImageView to visible
    }

    private fun onYoutubeClick() { // Set the onClickListener for the YouTube button
        binding.ivYoutube.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(meal.youtubeUrl)
            ) // Create an intent to open the YouTube link
            startActivity(intent) // Start the intent
        }
    }

    private fun onFavouriteClick() { // Set the onClickListener for the favourite button
        mealViewModel.getMealByIdFromDb(mealId).observe(this) { favouriteMeal ->
            if (favouriteMeal != null) { // If the meal is not null (it exists in the database)
                binding.fabAddToFavourites.setOnClickListener {
                    mealViewModel.deleteMealFromDb(meal) // Delete the meal from the database
                    binding.fabAddToFavourites.setImageResource(R.drawable.ic_favourite_border) // Set the FAB icon to the favourite border icon
                }
            } else { // If the meal is null (it does not exist in the database)
                binding.fabAddToFavourites.setOnClickListener {
                    mealViewModel.insertMealIntoDb(meal) // Insert the meal into the database
                    binding.fabAddToFavourites.setImageResource(R.drawable.ic_favourite) // Set the FAB icon to the favourite icon
                }
            }
        }
    }
}