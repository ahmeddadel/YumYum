package com.dolla.yumyum.data.retrofit

import com.dolla.yumyum.util.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @created 26/03/2023 - 5:51 PM
 * @project YumYum
 * @author adell
 */

// This is a singleton class that will be used to create a Retrofit instance for the MealDB API
object RetrofitInstance {

    // retrofit instance for random mealApi call to the MealDB API
    // lazy means that the instance will only be created when it is first used
    val mealApi: IMealApi by lazy {
        Retrofit.Builder() // Create a Retrofit instance
            .baseUrl(BASE_URL) // Set the base URL
            .addConverterFactory(
                GsonConverterFactory.create() // Add the Gson converter factory
            )
            .build() // Build the Retrofit instance
            .create(IMealApi::class.java) // Create the API interface
    }
}