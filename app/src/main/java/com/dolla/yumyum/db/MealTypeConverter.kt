package com.dolla.yumyum.db

import androidx.room.TypeConverter
import androidx.room.TypeConverters

/**
 * @created 03/04/2023 - 7:58 PM
 * @project YumYum
 * @author adell
 */

// MealTypeConverter is used to convert the data type of the meal
@TypeConverters // TypeConverters is used to indicate that the class is used to convert the data type of the meal
class MealTypeConverter {

    @TypeConverter
    fun fromAnyToString(attribute: Any?): String { // fromAnyToString is used to convert the data type of the meal from Any to String
        if (attribute == null) { // if the attribute is null
            return "" // return an empty string
        }
        return attribute.toString() // return the attribute as a string
    }

    @TypeConverter
    fun fromStringToAny(attribute: String?): Any { // fromStringToAny is used to convert the data type of the meal from String to Any
        if (attribute == null) { // if the attribute is null
            return "" // return null
        }
        return attribute // return the attribute
    }
}