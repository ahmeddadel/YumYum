package com.dolla.yumyum.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dolla.yumyum.db.MealDatabase
import com.dolla.yumyum.pojo.Meal
import com.dolla.yumyum.pojo.MealList
import com.dolla.yumyum.retrofit.RetrofitInstance
import kotlinx.coroutines.launch

/**
 * @created 30/03/2023 - 1:49 AM
 * @project YumYum
 * @author adell
 */

class MealViewModel(private val mealDatabase: MealDatabase) : ViewModel() {

    private val _mealLiveData = MutableLiveData<Meal>()
    val mealDetailsLiveData: LiveData<Meal>
        get() = _mealLiveData // This is a read-only property that returns the value of the private property _mealLiveData

    fun getMealById(id: String) { // This function will make the API call to get the meal details by id
        RetrofitInstance.mealApi.getMealById(id)
            .enqueue(object : retrofit2.Callback<MealList> { // Make the API call
                override fun onResponse(
                    call: retrofit2.Call<MealList>,
                    response: retrofit2.Response<MealList>
                ) {
                    if (response.isSuccessful) { // If the response is successful, get the list of meals from the response body
                        val mealList =
                            response.body()?.meals // The response body is a MealList object
                        _mealLiveData.value =
                            mealList?.get(0) // Set the value of the mealLiveData to the first meal in the list (the meal)
                    }
                }

                override fun onFailure(
                    call: retrofit2.Call<MealList>,
                    t: Throwable
                ) { // If the API call fails, log the error message
                    Log.d("MealActivity_getMealById()", t.message.toString())
                }
            })
    }

    fun insertMeal(meal: Meal) { // This function will insert the meal into the database
        viewModelScope.launch { // viewModelScope is used to launch a coroutine in the ViewModel
            mealDatabase.getMealDao()
                .upsertMeal(meal) // upsertMeal is used to update or insert the meal into the database
        }
    }

    fun deleteMeal(meal: Meal) { // This function will delete the meal from the database
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