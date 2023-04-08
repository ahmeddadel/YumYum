package com.dolla.yumyum.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dolla.yumyum.data.pojo.PopularMeal
import com.dolla.yumyum.databinding.PopularMealItemBinding

/**
 * @created 31/03/2023 - 5:55 PM
 * @project YumYum
 * @author adell
 */

class PopularMealsAdapter :
    RecyclerView.Adapter<PopularMealsAdapter.PopularMealsViewHolder>() { // Adapter class for the RecyclerView in the HomeFragment

    // lambda functions that will be used to handle the click event on the popular meal item in the RecyclerView
    lateinit var onPopularMealClicked: ((PopularMeal) -> Unit) // This function will be used to handle the click event on the popular meal item
    lateinit var onPopularMealLongClicked: ((PopularMeal) -> Unit) // This function will be used to handle the long click event on the popular meal item
    private var popularMealList =
        ArrayList<PopularMeal>() // This list will hold the list of popular meals

    fun setMealsList(mealsList: ArrayList<PopularMeal>) { // This function will be used to set the list of meals to the adapter and notify the adapter that the data set has changed
        this.popularMealList = mealsList
        notifyDataSetChanged() // Notify the adapter that the data set has changed
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PopularMealsViewHolder { // This function will be called to create a new ViewHolder object instance and return it to the RecyclerView when it needs a new item view to display.
        // Initialize the view binding object instance using the layout file name
        return PopularMealsViewHolder(
            PopularMealItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: PopularMealsViewHolder,
        position: Int
    ) { // This function will be called to bind the data to the view holder object instance
        Glide.with(holder.itemView) // holder.itemView is the root view of the item view in the RecyclerView (popular_items.xml)
            .load(popularMealList[position].thumbUrl) // Load the image from the URL
            .into(holder.binding.ivPopularItem) // Set the image to the ImageView

        holder.itemView.setOnClickListener { // Set the click listener on the root view of the item view in the RecyclerView (popular_items.xml)
            onPopularMealClicked(popularMealList[position]) // Call the lambda function to handle the click event on the popular meal item
        }
        holder.itemView.setOnLongClickListener { // Set the long click listener on the root view of the item view in the RecyclerView (popular_items.xml)
            onPopularMealLongClicked(popularMealList[position]) // Call the lambda function to handle the long click event on the popular meal item
            true // Return true to indicate that the long click event has been handled
        }
    }

    override fun getItemCount(): Int =
        popularMealList.size // This function will return the number of items in the list

    // ViewHolder class that takes in the view binding object instance of the popular_items.xml layout file and extends the RecyclerView.ViewHolder class to hold the view binding object instance
    inner class PopularMealsViewHolder(val binding: PopularMealItemBinding) :
        RecyclerView.ViewHolder(binding.root) // ViewHolder class for the PopularMealsAdapter class to hold the view binding object instance of the popular_items.xml layout file

}