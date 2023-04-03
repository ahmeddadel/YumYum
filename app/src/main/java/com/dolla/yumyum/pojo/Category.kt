package com.dolla.yumyum.pojo

import com.google.gson.annotations.SerializedName

data class Category( // serializedName is used to map the JSON key to the variable name
    @SerializedName("idCategory") val id: String,
    @SerializedName("strCategory") val name: String,
    @SerializedName("strCategoryDescription") val description: String,
    @SerializedName("strCategoryThumb") val thumbUrl: String
)