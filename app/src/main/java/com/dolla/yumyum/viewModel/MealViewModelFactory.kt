package com.dolla.yumyum.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dolla.yumyum.data.db.MealDatabase

/**
 * @created 04/04/2023 - 8:30 AM
 * @project YumYum
 * @author adell
 */

class MealViewModelFactory(private val mealDatabase: MealDatabase) :
    ViewModelProvider.Factory { // Class that is responsible for creating the MealViewModel
    override fun <T : ViewModel> create(modelClass: Class<T>): T { // This function will create the MealViewModel and pass the MealDatabase to it
        return MealViewModel(mealDatabase) as T // Return the MealViewModel as T (the type of the ViewModel)
    }
}