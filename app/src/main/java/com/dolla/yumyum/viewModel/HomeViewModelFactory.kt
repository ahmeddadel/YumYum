package com.dolla.yumyum.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dolla.yumyum.data.db.MealRepository

/**
 * @created 04/04/2023 - 8:46 AM
 * @project YumYum
 * @author adell
 */

class HomeViewModelFactory(private val mealRepository: MealRepository) :
    ViewModelProvider.Factory { // Class that is responsible for creating the HomeViewModel
    override fun <T : ViewModel> create(modelClass: Class<T>): T { // This function will create the HomeViewModel
        return HomeViewModel(mealRepository) as T // Return the HomeViewModel as T (the type of the ViewModel)
    }
}