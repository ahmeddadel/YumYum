package com.dolla.yumyum.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dolla.yumyum.databinding.CategoryMealItemBinding
import com.dolla.yumyum.pojo.MealsByCategory

/**
 * @created 03/04/2023 - 3:17 AM
 * @project YumYum
 * @author adell
 */

class CategoryMealsAdapter :
    RecyclerView.Adapter<CategoryMealsAdapter.CategoryMealsViewHolder>() { // Adapter class for the RecyclerView in the CategoryMealsActivity

    // lambda function that will be used to handle the click event on the category meal item in the RecyclerView
    lateinit var onCategoryMealClicked: ((MealsByCategory) -> Unit)// This function will be called when a category meal is clicked
    private var categoryMealsList = ArrayList<MealsByCategory>() // List of category meals

    fun setCategoryMealsList(categoryMealsList: ArrayList<MealsByCategory>) { // This function will be used to set the list of category meals to the adapter and notify the adapter that the data set has changed
        this.categoryMealsList = categoryMealsList
        notifyDataSetChanged() // Notify the adapter that the data set has changed
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryMealsViewHolder { // This function will be called to create a new ViewHolder object instance and return it to the RecyclerView when it needs a new item view to display.
        // Initialize the view binding object instance using the layout file name
        return CategoryMealsViewHolder(
            CategoryMealItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: CategoryMealsViewHolder,
        position: Int
    ) { // This function will be called to bind the data to the view holder object instance
        Glide.with(holder.itemView) // holder.itemView is the root view of the item view in the RecyclerView (popular_items.xml)
            .load(categoryMealsList[position].thumbUrl) // Load the image from the URL
            .into(holder.binding.ivMeal) // Set the image to the ImageView

        holder.binding.tvMealTitle.text =
            categoryMealsList[position].name // Set the category meal name to the TextView

        holder.itemView.setOnClickListener { // Set the click listener on the root view of the item view in the RecyclerView (popular_items.xml)
            onCategoryMealClicked(categoryMealsList[position]) // Call the lambda function to handle the click event on the category meal item
        }
    }

    override fun getItemCount(): Int =
        categoryMealsList.size // This function will return the number of items in the list

    // ViewHolder class that takes in the view binding object instance of the popular_items.xml layout file and extends the RecyclerView.ViewHolder class to hold the view binding object instance
    inner class CategoryMealsViewHolder(val binding: CategoryMealItemBinding) :
        RecyclerView.ViewHolder(binding.root) // View holder class for the RecyclerView in the CategoryMealsActivity class to hold the view binding object instance of the category_meal_item.xml layout file
}