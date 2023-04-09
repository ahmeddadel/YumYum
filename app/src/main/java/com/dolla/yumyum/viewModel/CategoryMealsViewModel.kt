package com.dolla.yumyum.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dolla.yumyum.data.pojo.MealsByCategoryList
import com.dolla.yumyum.data.retrofit.RetrofitInstance
import kotlinx.coroutines.*

/**
 * @created 03/04/2023 - 2:58 AM
 * @project YumYum
 * @author adell
 */

class CategoryMealsViewModel : ViewModel() {

    private var job: Job? = null

    override fun onCleared() {
        super.onCleared()
        job?.cancel() // This will cancel the job when the view model is destroyed
    }

    private val _categoryMealsLiveData = MutableLiveData<MealsByCategoryList?>()
    val categoryMealsLiveData: MutableLiveData<MealsByCategoryList?>
        get() = _categoryMealsLiveData // This is a read-only property that returns the value of the private property _categoryMealsLiveData

    fun getMealsByCategory(category: String) { // This function will make the API call to get the meals by category
        job =
            CoroutineScope(Dispatchers.IO).launch { // Create a coroutine scope and launch a coroutine in the IO dispatcher to make the API call
                val mealsByCategoryResponse =
                    RetrofitInstance.mealApi.getMealByCategory(category) // Make the API call
                withContext(Dispatchers.Main) { // Switch to the main dispatcher to set the value of the categoryMealsLiveData
                    if (mealsByCategoryResponse.isSuccessful) { // If the response is successful, get the list of meals from the response body
                        val mealsByCategoryList =
                            mealsByCategoryResponse.body() // The response body is a MealsByCategoryList object
                        _categoryMealsLiveData.value =
                            mealsByCategoryList // Set the value of the categoryMealsLiveData to the list of meals by category
                    } else {
                        Log.d(
                            "CategoryMealsViewModel_getMealsByCategory()",
                            mealsByCategoryResponse.message()
                        ) // If the API call fails, log the error message
                    }
                }
            }
    }
}