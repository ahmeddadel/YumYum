package com.dolla.yumyum.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dolla.yumyum.data.db.MealRepository
import com.dolla.yumyum.data.pojo.CategoryList
import com.dolla.yumyum.data.pojo.Meal
import com.dolla.yumyum.data.pojo.PopularMealList
import com.dolla.yumyum.data.retrofit.RetrofitInstance
import com.dolla.yumyum.util.Constants.RETRY_DELAY_MILLIS
import com.dolla.yumyum.util.Constants.getRandomCategoryName
import kotlinx.coroutines.*

/**
 * @created 28/03/2023 - 5:07 PM
 * @project YumYum
 * @author adell
 */

class HomeViewModel(private val mealRepository: MealRepository) : ViewModel() {

    private var job: Job? = null
    private var _randomCategoryName: String // This will get a random category name


    override fun onCleared() {
        super.onCleared()
        job?.cancel() // This will cancel the job when the view model is destroyed
    }

    private val _randomMealLiveData = MutableLiveData<Meal?>()
    val randomMealLiveData: LiveData<Meal?>
        get() = _randomMealLiveData // This is a read-only property that returns the value of the private property _randomMealLiveData

    private val _popularMealsLiveData = MutableLiveData<PopularMealList?>()
    val popularMealsLiveData: LiveData<PopularMealList?>
        get() = _popularMealsLiveData // This is a read-only property that returns the value of the private property _popularMealsLiveData

    private val _categoriesLiveData = MutableLiveData<CategoryList?>()
    val categoriesLiveData: LiveData<CategoryList?>
        get() = _categoriesLiveData // This is a read-only property that returns the value of the private property _categoriesLiveData

    private val _favouriteMealsLiveData =
        mealRepository.allMeals// This will get all the meals from the database and store it in the _favouriteMealsLiveData
    val favouriteMealsLiveData: LiveData<List<Meal>>
        get() = _favouriteMealsLiveData // This is a read-only property that returns the value of the private property _favouriteMealsLiveData

    private val _noFavouriteMealsLiveData =
        MutableLiveData(false) // This will be used to show a message when there are no favourite meals
    val noFavouriteMealsLiveData: LiveData<Boolean>
        get() = _noFavouriteMealsLiveData // This is a read-only property that returns the value of the private property _noFavouriteMealsLiveData

    private val _mealBottomSheetDialogLiveData = MutableLiveData<Meal?>()
    val mealBottomSheetDialogLiveData: LiveData<Meal?>
        get() = _mealBottomSheetDialogLiveData // This is a read-only property that returns the value of the private property _bottomSheetMealLiveData

    private val _searchedMealLiveData = MutableLiveData<List<Meal>?>()
    val searchedMealLiveData: LiveData<List<Meal>?>
        get() = _searchedMealLiveData // This is a read-only property that returns the value of the private property _searchedMealLiveData

    private var saveStateRandomMeal: Meal? =
        null // This is a variable that will be used to save the state of the random meal when the fragment is destroyed and recreated

    init { // init block will be called when the view model is created
        _randomCategoryName = getRandomCategoryName()
    }

    fun getRandomMeal() { // This function will make the API call to get a random meal
        saveStateRandomMeal?.let { randomMeal -> // If the saveStateRandomMeal is not null, set the value of the randomMealLiveData to the saveStateRandomMeal
            _randomMealLiveData.value = randomMeal
            return // Return from the function so that the API call is not made
        }

        job =
            CoroutineScope(Dispatchers.IO).launch { // Create a coroutine scope and launch a coroutine in the IO dispatcher to make the API call
                while (true) {
                    try {
                        val randomMealResponse =
                            RetrofitInstance.mealApi.getRandomMeal() // Make the API call
                        withContext(Dispatchers.Main) { // Switch to the main dispatcher to set the value of the randomMealLiveData
                            if (randomMealResponse.isSuccessful) { // If the response is successful, get the list of meals from the response body
                                val randomMealList =
                                    randomMealResponse.body()?.meals // The response body is a MealList object
                                _randomMealLiveData.value =
                                    randomMealList?.get(0) // Set the value of the randomMealLiveData to the first meal in the list (the random meal)
                                saveStateRandomMeal =
                                    randomMealList?.get(0) // Save the state of the random meal
                            } else {
                                Log.d(
                                    "HomeFragment_getRandomMeal()",
                                    randomMealResponse.message()
                                ) // If the API call fails, log the error message
                            }
                        }
                        break // if the API call is successful, break out of the loop
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            _randomMealLiveData.value =
                                null // If there is an error, set the value of the randomMealLiveData to null
                            Log.d("HomeFragment_getRandomMeal()", e.message.toString())
                        }
                    }
                    delay(RETRY_DELAY_MILLIS) // If the API call fails, delay the retry function by the retryDelayMillis
                }
            }
    }

    fun getPopularMeals() { // This function will make the API call to get the popular meals
        job =
            CoroutineScope(Dispatchers.IO).launch { // Create a coroutine scope and launch a coroutine in the IO dispatcher to make the API call
                while (true) {
                    try {
                        val popularMealResponse =
                            RetrofitInstance.mealApi.getPopularMeals(_randomCategoryName) // Make the API call
                        withContext(Dispatchers.Main) { // Switch to the main dispatcher to set the value of the popularMealsLiveData
                            if (popularMealResponse.isSuccessful) { // If the response is successful, get the list of meals from the response body
                                val popularMealList =
                                    popularMealResponse.body() // The response body is a PopularMealList object
                                _popularMealsLiveData.value =
                                    popularMealList // Set the value of the popularMealsLiveData to the list of popular meals
                            } else {
                                Log.d(
                                    "HomeFragment_getPopularMeals()",
                                    popularMealResponse.message()
                                ) // If the API call fails, log the error message
                            }
                        }
                        break // if the API call is successful, break out of the loop
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            _popularMealsLiveData.value =
                                null // If there is an error, set the value of the popularMealsLiveData to null
                            Log.d("HomeFragment_getPopularMeals()", e.message.toString())
                        }
                    }
                    delay(RETRY_DELAY_MILLIS) // If the API call fails, delay the retry function by the retryDelayMillis
                }
            }
    }

    fun getCategories() { // This function will make the API call to get all the categories
        job =
            CoroutineScope(Dispatchers.IO).launch { // Create a coroutine scope and launch a coroutine in the IO dispatcher to make the API call
                while (true) {
                    try {
                        val categoryResponse =
                            RetrofitInstance.mealApi.getCategories() // Make the API call
                        withContext(Dispatchers.Main) { // Switch to the main dispatcher to set the value of the categoriesLiveData
                            if (categoryResponse.isSuccessful) { // If the response is successful, get the list of meals from the response body
                                val categoryList =
                                    categoryResponse.body() // The response body is a CategoryList object
                                _categoriesLiveData.value =
                                    categoryList // Set the value of the categoriesLiveData to the list of categories
                            } else {
                                Log.d(
                                    "HomeFragment_getCategories()",
                                    categoryResponse.message()
                                ) // If the API call fails, log the error message
                            }
                        }
                        break // if the API call is successful, break out of the loop
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            _categoriesLiveData.value =
                                null // If there is an error, set the value of the categoriesLiveData to null
                            Log.d("HomeFragment_getCategories()", e.message.toString())
                        }
                    }
                    delay(RETRY_DELAY_MILLIS) // If the API call fails, delay the retry function by the retryDelayMillis
                }
            }
    }

    fun getMealById(id: String) { // This function will make the API call to get a meal by its ID
        job =
            CoroutineScope(Dispatchers.IO).launch { // Create a coroutine scope and launch a coroutine in the IO dispatcher to make the API call
                while (true) {
                    try {
                        val mealResponse =
                            RetrofitInstance.mealApi.getMealById(id) // Make the API call
                        withContext(Dispatchers.Main) { // Switch to the main dispatcher to set the value of the mealBottomSheetDialogLiveData
                            if (mealResponse.isSuccessful) { // If the response is successful, get the list of meals from the response body
                                val mealList =
                                    mealResponse.body()?.meals // The response body is a MealList object
                                _mealBottomSheetDialogLiveData.value =
                                    mealList?.get(0) // Set the value of the mealBottomSheetDialogLiveData to the first meal in the list (the meal with the specified ID)
                            } else {
                                Log.d(
                                    "HomeFragment_getMealById()",
                                    mealResponse.message()
                                ) // If the API call fails, log the error message
                            }
                        }
                        break // if the API call is successful, break out of the loop
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            _mealBottomSheetDialogLiveData.value =
                                null // If there is an error, set the value of the mealBottomSheetDialogLiveData to null
                            Log.d("HomeFragment_getMealById()", e.message.toString())
                        }
                    }
                    delay(RETRY_DELAY_MILLIS) // If the API call fails, delay the retry function by the retryDelayMillis
                }
            }
    }

    fun searchMealByName(name: String) { // This function will make the API call to search a meal by its name
        job =
            CoroutineScope(Dispatchers.IO).launch { // Create a coroutine scope and launch a coroutine in the IO dispatcher to make the API call
                while (true) {
                    try {
                        val mealResponse =
                            RetrofitInstance.mealApi.searchMealByName(name) // Make the API call
                        withContext(Dispatchers.Main) { // Switch to the main dispatcher to set the value of the searchedMealLiveData
                            if (mealResponse.isSuccessful) { // If the response is successful, get the list of meals from the response body
                                val mealList =
                                    mealResponse.body() // The response body is a MealList object
                                _searchedMealLiveData.value =
                                    mealList?.meals // Set the value of the searchedMealLiveData to the list of meals
                            } else {
                                Log.d(
                                    "HomeFragment_searchMealByName()",
                                    mealResponse.message()
                                ) // If the API call fails, log the error message
                            }
                        }
                        break // if the API call is successful, break out of the loop
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            _searchedMealLiveData.value =
                                null // If there is an error, set the value of the searchedMealLiveData to null
                            Log.d("HomeFragment_searchMealByName()", e.message.toString())
                        }
                    }
                    delay(RETRY_DELAY_MILLIS) // If the API call fails, delay the retry function by the retryDelayMillis
                }
            }
    }

    fun setNoFavouriteMeals() { // This function will set the value of the noFavouriteMealsLiveData to true
        _noFavouriteMealsLiveData.value = true
    }

    fun setFavouriteMeals() { // This function will set the value of the noFavouriteMealsLiveData to false
        _noFavouriteMealsLiveData.value = false
    }

    fun deleteMealFromDb(meal: Meal) { // This function will delete the meal from the database
        viewModelScope.launch { // viewModelScope is used to launch a coroutine in the ViewModel
            mealRepository
                .deleteMeal(meal) // deleteMeal is used to delete the meal from the database
        }
    }

    fun insertMealIntoDb(meal: Meal) { // This function will insert the meal into the database
        viewModelScope.launch { // viewModelScope is used to launch a coroutine in the ViewModel
            mealRepository
                .upsertMeal(meal) // upsertMeal is used to update or insert the meal into the database
        }
    }
}