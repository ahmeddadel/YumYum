package com.dolla.yumyum.data.db

import androidx.lifecycle.LiveData
import com.dolla.yumyum.data.pojo.Meal

/**
 * @created 10/04/2023 - 4:08 AM
 * @project YumYum
 * @author adell
 */
class MealRepository(private val mealDao: MealDao) { // MealRepository is used to create a repository for the MealDao interface
    suspend fun upsertMeal(meal: Meal) = mealDao.upsertMeal(meal)

    suspend fun deleteMeal(meal: Meal) = mealDao.deleteMeal(meal)

    fun getMealById(id: String) = mealDao.getMealById(id)

    val allMeals: LiveData<List<Meal>> = mealDao.getAllMeals()
}