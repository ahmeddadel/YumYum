package com.dolla.yumyum.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.dolla.yumyum.databinding.FragmentMealBottomSheetBinding
import com.dolla.yumyum.ui.activites.MainActivity
import com.dolla.yumyum.ui.activites.MealActivity
import com.dolla.yumyum.ui.fragments.HomeFragment.Companion.MEAL_ID
import com.dolla.yumyum.ui.fragments.HomeFragment.Companion.MEAL_NAME
import com.dolla.yumyum.ui.fragments.HomeFragment.Companion.MEAL_THUMB
import com.dolla.yumyum.viewModel.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

private const val MEAL_ID_PARAM = "mealId"

class MealBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentMealBottomSheetBinding
    private lateinit var viewModel: HomeViewModel
    private var mealId: String? = null
    private var mealName: String? = null
    private var mealThumb: String? = null

    override fun onCreate(savedInstanceState: Bundle?) { // This method is called when the fragment is first created
        super.onCreate(savedInstanceState)

        // Get the HomeViewModel instance from the MainActivity
        viewModel = (activity as MainActivity).viewModel

        // Get the mealId from the arguments
        arguments?.let {
            mealId = it.getString(MEAL_ID_PARAM)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View { // This method is called to have the fragment instantiate its user interface view
        // Inflate the layout for this fragment
        binding = FragmentMealBottomSheetBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) { // This method is called after onCreateView when the fragment's view hierarchy is created and ready to be used
        super.onViewCreated(view, savedInstanceState)

        mealId?.let { viewModel.getMealById(it) } // Call the getMealById() method from the HomeViewModel (fire the API call)
        observeMealBottomSheetDialog() // Observe the bottomSheetMeal LiveData
        onMealBottomSheetDialogClick() // Observe the bottomSheetMealClick LiveData
    }

    private fun observeMealBottomSheetDialog() { // Observe the bottomSheetMealLiveData of the HomeViewModel
        viewModel.mealBottomSheetDialogLiveData.observe(viewLifecycleOwner) { bottomSheetMeal ->
            Glide.with(this@MealBottomSheetDialogFragment) // this@MealBottomSheetFragment is the context of the fragment
                .load(bottomSheetMeal.thumbUrl) // Load the meal image from the URL
                .into(binding.ivBottomSheetMealImage) // Set the image to the ImageView

            binding.tvBottomSheetMealName.text =
                bottomSheetMeal.name // Set the meal name to the TextView
            binding.tvBottomSheetMealCategory.text =
                bottomSheetMeal.category // Set the meal category to the TextView
            binding.tvBottomSheetMealLocation.text =
                bottomSheetMeal.area // Set the meal area to the TextView

            mealName = bottomSheetMeal.name // Set the meal name to the mealName variable
            mealThumb = bottomSheetMeal.thumbUrl // Set the meal thumbUrl to the mealThumb variable
        }
    }

    private fun onMealBottomSheetDialogClick() { // Set the onClickListener to the bottomSheetMeal layout
        binding.mealBottomSheet.setOnClickListener {
            if (mealName != null && mealThumb != null) {
                val intent =
                    Intent(
                        activity,
                        MealActivity::class.java
                    ) // Create an intent to the MainActivity
                intent.apply {
                    putExtra(MEAL_ID, mealId) // Pass the mealId to the MainActivity
                    putExtra(MEAL_NAME, mealName) // Pass the mealName to the MainActivity
                    putExtra(MEAL_THUMB, mealThumb) // Pass the mealThumb to the MainActivity

                    startActivity(intent) // Start the MainActivity
                    dismiss() // Dismiss the bottomSheet dialog fragment
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(mealId: String) =
            MealBottomSheetDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(MEAL_ID_PARAM, mealId)
                }
            }
    }
}