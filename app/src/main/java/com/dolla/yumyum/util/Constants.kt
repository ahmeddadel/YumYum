package com.dolla.yumyum.util

import java.util.*

/**
 * @created 26/03/2023 - 9:27 PM
 * @project YumYum
 * @author adell
 */

object Constants { // Object that contains all the constants used in the app (Singleton)
    const val BASE_URL = "https://www.themealdb.com/api/json/v1/1/" // Base URL for the MealDB API
    const val RANDOM_MEAL = "random.php" // Path for the random meal API call
    const val MEAL_BY_ID = "lookup.php?" // Path for the meal by id API call
    const val MEAL_BY_NAME = "search.php?" // Path for the meal by name API call
    const val POPULAR_MEALS = "filter.php?" // Path for the popular meals API call
    const val CATEGORIES = "categories.php" // Path for the categories API call
    const val MEAL_BY_CATEGORY = "filter.php?" // Path for the meal by category API call
    const val TABLE_NAME = "mealInformation" // Table name for the Room Database
    const val PRIMARY_KEY = "id" // Primary key for the Room Database
    const val DATABASE_NAME = "mealDatabase" // Database name for the Room Database
    const val RETRY_DELAY_MILLIS = 3000L // Delay time for the retry function

    // Function that returns a random category from the list of categories
    fun getRandomCategoryName(): String {
        val categoriesList = listOf( // List of all categories
            "Beef",
            "Chicken",
            "Dessert",
            "Lamb",
            "Miscellaneous",
            "Pasta",
            "Pork",
            "Seafood",
            "Side",
            "Starter",
            "Vegan",
            "Vegetarian",
            "Breakfast",
            "Goat"
        )
        val random = Random() // Random object
        val randomIndex = random.nextInt(categoriesList.size) // Random index
        return categoriesList[randomIndex] // Return the random category
    }
}