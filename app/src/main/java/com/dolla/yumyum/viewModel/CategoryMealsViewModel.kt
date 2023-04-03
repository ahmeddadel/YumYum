package com.dolla.yumyum.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dolla.yumyum.pojo.MealsByCategory
import com.dolla.yumyum.pojo.MealsByCategoryList
import com.dolla.yumyum.retrofit.RetrofitInstance

/**
 * @created 03/04/2023 - 2:58 AM
 * @project YumYum
 * @author adell
 */

class CategoryMealsViewModel : ViewModel() {

    private val _categoryMealsLiveData = MutableLiveData<List<MealsByCategory>?>()
    val categoryMealsLiveData: MutableLiveData<List<MealsByCategory>?>
        get() = _categoryMealsLiveData // This is a read-only property that returns the value of the private property _categoryMealsLiveData

    fun getMealsByCategory(category: String) { // This function will make the API call to get the meals by category
        RetrofitInstance.mealApi.getMealByCategory(category)
            .enqueue(object : retrofit2.Callback<MealsByCategoryList> { // Make the API call
                override fun onResponse(
                    call: retrofit2.Call<MealsByCategoryList>,
                    response: retrofit2.Response<MealsByCategoryList>
                ) {
                    if (response.isSuccessful) { // If the response is successful, get the list of meals from the response body
                        val mealsByCategoryList =
                            response.body()?.meals // The response body is a MealsByCategoryList object
                        _categoryMealsLiveData.value =
                            mealsByCategoryList // Set the value of the categoryMealsLiveData to the list of meals by category
                    }
                }

                override fun onFailure(
                    call: retrofit2.Call<MealsByCategoryList>,
                    t: Throwable
                ) {
                    Log.d("CategoryMealsViewModel_getMealsByCategory()", t.message.toString())
                }
            })
    }
}