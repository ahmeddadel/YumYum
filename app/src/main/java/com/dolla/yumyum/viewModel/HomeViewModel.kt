package com.dolla.yumyum.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dolla.yumyum.db.MealDatabase
import com.dolla.yumyum.pojo.*
import com.dolla.yumyum.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @created 28/03/2023 - 5:07 PM
 * @project YumYum
 * @author adell
 */

class HomeViewModel(mealDatabase: MealDatabase) : ViewModel() {

    private val _randomMealLiveData = MutableLiveData<Meal>()
    val randomMealLiveData: LiveData<Meal>
        get() = _randomMealLiveData // This is a read-only property that returns the value of the private property _randomMealLiveData

    private val _popularMealsLiveData = MutableLiveData<List<PopularMeal>?>()
    val popularMealsLiveData: LiveData<List<PopularMeal>?>
        get() = _popularMealsLiveData // This is a read-only property that returns the value of the private property _popularMealsLiveData

    private val _categoriesLiveData = MutableLiveData<List<Category>?>()
    val categoriesLiveData: LiveData<List<Category>?>
        get() = _categoriesLiveData // This is a read-only property that returns the value of the private property _categoriesLiveData

    private val _favouriteMealsLiveData = mealDatabase.getMealDao()
        .getAllMeals() // This will get all the meals from the database and store it in the _favouriteMealsLiveData
    val favouriteMealsLiveData: LiveData<List<Meal>>
        get() = _favouriteMealsLiveData // This is a read-only property that returns the value of the private property _favouriteMealsLiveData

    fun getRandomMeal() { // This function will make the API call to get a random meal
        RetrofitInstance.mealApi.getRandomMeal()
            .enqueue(object : Callback<MealList> { // Make the API call
                override fun onResponse(call: Call<MealList>, response: Response<MealList>) {

                    if (response.isSuccessful) { // If the response is successful, get the list of meals from the response body
                        val randomMealList =
                            response.body()?.meals // The response body is a MealList object
                        _randomMealLiveData.value =
                            randomMealList?.get(0)// Set the value of the randomMealLiveData to the first meal in the list (the random meal)
                    }
                }

                override fun onFailure(
                    call: Call<MealList>,
                    t: Throwable
                ) { // If the API call fails, log the error message
                    Log.d("HomeFragment_getRandomMeal()", t.message.toString())
                }
            })
    }

    fun getPopularMeals() { // This function will make the API call to get the popular meals
        RetrofitInstance.mealApi.getPopularMeals("Seafood") // We will use the category "Seafood" to get the popular meals because the MealDB API for popular meals is not free
            .enqueue(object : Callback<PopularMealList> { // Make the API call
                override fun onResponse(
                    call: Call<PopularMealList>,
                    response: Response<PopularMealList>
                ) {
                    if (response.isSuccessful) { // If the response is successful, get the list of meals from the response body
                        val popularMealList =
                            response.body()?.meals // The response body is a CategoryList object
                        _popularMealsLiveData.value =
                            popularMealList // Set the value of the popularMealsLiveData to the list of popular meals

                    }
                }

                override fun onFailure(
                    call: Call<PopularMealList>,
                    t: Throwable
                ) { // If the API call fails, log the error message
                    Log.d("HomeFragment_getPopularMeals()", t.message.toString())
                }
            })
    }

    fun getCategories() { // This function will make the API call to get all the categories
        RetrofitInstance.mealApi.getCategories()
            .enqueue(object : Callback<CategoryList> { // Make the API call
                override fun onResponse(
                    call: Call<CategoryList>,
                    response: Response<CategoryList>
                ) {
                    if (response.isSuccessful) { // If the response is successful, get the list of meals from the response body
                        val categoryList =
                            response.body()?.categories// The response body is a CategoryList object
                        _categoriesLiveData.value =
                            categoryList // Set the value of the categoriesLiveData to the list of categories
                    }
                }

                override fun onFailure(
                    call: Call<CategoryList>,
                    t: Throwable
                ) { // If the API call fails, log the error message
                    Log.d("HomeFragment_getCategories()", t.message.toString())
                }
            })
    }
}