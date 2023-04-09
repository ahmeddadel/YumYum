package com.dolla.yumyum.data.retrofit

import com.dolla.yumyum.data.pojo.CategoryList
import com.dolla.yumyum.data.pojo.MealList
import com.dolla.yumyum.data.pojo.MealsByCategoryList
import com.dolla.yumyum.data.pojo.PopularMealList
import com.dolla.yumyum.utils.Constants.CATEGORIES
import com.dolla.yumyum.utils.Constants.MEAL_BY_CATEGORY
import com.dolla.yumyum.utils.Constants.MEAL_BY_ID
import com.dolla.yumyum.utils.Constants.MEAL_BY_NAME
import com.dolla.yumyum.utils.Constants.POPULAR_MEALS
import com.dolla.yumyum.utils.Constants.RANDOM_MEAL
import retrofit2.Response
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
    suspend fun getRandomMeal(): Response<MealList> // function to get a random meal

    @GET(MEAL_BY_ID)
    suspend fun getMealById(@Query("i") id: String): Response<MealList> // function to get a meal by its id

    @GET(POPULAR_MEALS)
    suspend fun getPopularMeals(@Query("c") category: String): Response<PopularMealList> // function to get meals by category but we will use it to get the popular meals. because the MealDB API for popular meals is not free

    @GET(CATEGORIES)
    suspend fun getCategories(): Response<CategoryList> // function to get all the categories

    @GET(MEAL_BY_CATEGORY)
    suspend fun getMealByCategory(@Query("c") category: String): Response<MealsByCategoryList> // function to get meals by category

    @GET(MEAL_BY_NAME)
    suspend fun searchMealByName(@Query("s") name: String): Response<MealList> // function to get meals by name
}