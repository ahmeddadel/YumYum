package com.dolla.yumyum.retrofit

import com.dolla.yumyum.pojo.CategoryList
import com.dolla.yumyum.pojo.MealList
import com.dolla.yumyum.pojo.MealsByCategoryList
import com.dolla.yumyum.pojo.PopularMealList
import com.dolla.yumyum.utils.Constants.CATEGORIES
import com.dolla.yumyum.utils.Constants.MEAL_BY_CATEGORY
import com.dolla.yumyum.utils.Constants.MEAL_BY_ID
import com.dolla.yumyum.utils.Constants.POPULAR_MEALS
import com.dolla.yumyum.utils.Constants.RANDOM_MEAL
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @created 26/03/2023 - 5:48 PM
 * @project YumYum
 * @author adell
 */

// This is the interface that will be used to make the API call to the MealDB API
interface IMealApi {

    @GET(RANDOM_MEAL)
    fun getRandomMeal(): Call<MealList> // function to get a random meal

    @GET(MEAL_BY_ID)
    fun getMealById(@Query("i") id: String): Call<MealList> // function to get a meal by its id

    @GET(POPULAR_MEALS)
    fun getPopularMeals(@Query("c") category: String): Call<PopularMealList> // function to get meals by category but we will use it to get the popular meals. because the MealDB API for popular meals is not free

    @GET(CATEGORIES)
    fun getCategories(): Call<CategoryList> // function to get all the categories

    @GET(MEAL_BY_CATEGORY)
    fun getMealByCategory(@Query("c") category: String): Call<MealsByCategoryList> // function to get meals by category
}