package com.dolla.yumyum.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dolla.yumyum.pojo.Meal
import com.dolla.yumyum.utils.Constants.TABLE_NAME

/**
 * @created 03/04/2023 - 7:37 PM
 * @project YumYum
 * @author adell
 */

@Dao // Dao is used to create a data access object for Room Database
interface MealDao {
//    @Insert // Insert is used to insert data into the database
//    suspend fun insertMeal(meal: Meal) // suspend is used to make the function asynchronous
//
//    @Update // Update is used to update data in the database
//    suspend fun updateMeal(meal: Meal) // suspend is used to make the function asynchronous

    @Insert(onConflict = OnConflictStrategy.REPLACE) // Insert is used to insert data into the database and onConflict is used to replace the data if there is a conflict
    suspend fun upsertMeal(meal: Meal) // suspend is used to make the function asynchronous and upsert is used to update or insert data into the database

    @Delete // Delete is used to delete data from the database
    suspend fun deleteMeal(meal: Meal) // suspend is used to make the function asynchronous

    @Query("SELECT * FROM $TABLE_NAME") // Query is used to query data from the database
    fun getAllMeals(): LiveData<List<Meal>> // getAllMeals is used to get all the meals from the database (not suspend function because it will return a LiveData object)

    @Query("SELECT * FROM $TABLE_NAME WHERE id = :id") // Query is used to query data from the database
    fun getMealById(id: String): LiveData<Meal> // getMealById is used to get a meal by its id from the database (not suspend function because it will return a LiveData object)
}