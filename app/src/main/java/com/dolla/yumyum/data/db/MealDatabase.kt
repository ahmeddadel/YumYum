package com.dolla.yumyum.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dolla.yumyum.data.pojo.Meal
import com.dolla.yumyum.util.Constants.DATABASE_NAME

/**
 * @created 03/04/2023 - 7:45 PM
 * @project YumYum
 * @author adell
 */

// abstract class because we will have instances of MealDao interface and we don't want to write all the methods in the interface instead Room will do it for us
@Database(
    entities = [Meal::class],
    version = 1,
    exportSchema = false
) // Database is used to create a database for Room Database (entities is used to specify the entities that will be used in the database, version is used to specify the version of the database, exportSchema is used to specify whether the schema should be exported or not)
@TypeConverters(MealTypeConverter::class) // The MealTypeConverter class is used to convert the data type of the meal
abstract class MealDatabase : RoomDatabase() {
    // abstract fun because we will have instances of MealDao interface
    abstract fun getMealDao(): MealDao // getMealDao is used to get the MealDao interface

    companion object { // companion object is used to declare static variables and functions
        // @Volatile is used to make the variable visible to all the threads
        @Volatile
        private var instance: MealDatabase? =
            null // instance is used to store the instance of the MealDatabase class

        fun getInstance(context: Context): MealDatabase { // getInstance is used to get the instance of the MealDatabase class
            if (instance == null) { // if the instance is null
                synchronized(MealDatabase::class.java) { // synchronized is used to make the code block synchronized (only one thread can access the code block at a time)
                    if (instance == null) { // check again if the instance is null (to make sure that the instance is not created by another thread)
                        instance =
                            Room.databaseBuilder( // instance is assigned to the database builder
                                context, // context is used to get the application context
                                MealDatabase::class.java, // MealDatabase class is used to get the MealDatabase class
                                DATABASE_NAME // "mealDatabase" is used to specify the name of the database
                            )
                                .fallbackToDestructiveMigration() // fallbackToDestructiveMigration is used to fallback to destructive migration if the database version is changed (rebuild the database and keep the data)
                                .build() // build is used to build the database
                    }
                }
            }
            return instance as MealDatabase // return the instance of the MealDatabase class
        }
    }
}