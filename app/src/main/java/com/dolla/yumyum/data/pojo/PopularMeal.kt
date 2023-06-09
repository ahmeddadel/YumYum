package com.dolla.yumyum.data.pojo

import com.google.gson.annotations.SerializedName

data class PopularMeal( // serializedName is used to map the JSON key to the variable name
    @SerializedName("idMeal") val id: String,
    @SerializedName("strMeal") val name: String,
    @SerializedName("strMealThumb") val thumbUrl: String
)