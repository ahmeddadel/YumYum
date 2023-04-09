package com.dolla.yumyum.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dolla.yumyum.data.db.MealDatabase
import com.dolla.yumyum.data.pojo.Meal
import com.dolla.yumyum.data.retrofit.RetrofitInstance
import kotlinx.coroutines.*

/**
 * @created 30/03/2023 - 1:49 AM
 * @project YumYum
 * @author adell
 */

class MealViewModel(private val mealDatabase: MealDatabase) : ViewModel() {

    private var job: Job? = null

    override fun onCleared() {
        super.onCleared()
        job?.cancel() // This will cancel the job when the view model is destroyed
    }

    private val _mealLiveData = MutableLiveData<Meal>()
    val mealDetailsLiveData: LiveData<Meal>
        get() = _mealLiveData // This is a read-only property that returns the value of the private property _mealLiveData

    fun getMealById(id: String) { // This function will make the API call to get a meal by its ID
        job =
            CoroutineScope(Dispatchers.IO).launch { // Create a coroutine scope and launch a coroutine in the IO dispatcher to make the API call
                val mealResponse =
                    RetrofitInstance.mealApi.getMealById(id) // Make the API call
                withContext(Dispatchers.Main) { // Switch to the main dispatcher to set the value of the mealBottomSheetDialogLiveData
                    if (mealResponse.isSuccessful) { // If the response is successful, get the list of meals from the response body
                        val mealList =
                            mealResponse.body()?.meals // The response body is a MealList object
                        _mealLiveData.value =
                            mealList?.get(0) // Set the value of the mealBottomSheetDialogLiveData to the first meal in the list (the meal with the specified ID)
                    } else {
                        Log.d(
                            "HomeFragment_getMealById()",
                            mealResponse.message()
                        ) // If the API call fails, log the error message
                    }
                }
            }
    }

    fun insertMealIntoDb(meal: Meal) { // This function will insert the meal into the database
        viewModelScope.launch { // viewModelScope is used to launch a coroutine in the ViewModel
            mealDatabase.getMealDao()
                .upsertMeal(meal) // upsertMeal is used to update or insert the meal into the database
        }
    }

    fun deleteMealFromDb(meal: Meal) { // This function will delete the meal from the database
        viewModelScope.launch { // viewModelScope is used to launch a coroutine in the ViewModel
            mealDatabase.getMealDao()
                .deleteMeal(meal) // deleteMeal is used to delete the meal from the database
        }
    }

    fun getMealByIdFromDb(id: String): LiveData<Meal> { // This function will get the meal from the database by id
        return mealDatabase.getMealDao()
            .getMealById(id) // getMealById is used to get the meal from the database by id
    }
}