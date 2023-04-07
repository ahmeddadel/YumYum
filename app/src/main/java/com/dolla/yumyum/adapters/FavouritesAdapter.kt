package com.dolla.yumyum.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dolla.yumyum.databinding.CategoryMealItemBinding
import com.dolla.yumyum.pojo.Meal

/**
 * @created 04/04/2023 - 9:01 AM
 * @project YumYum
 * @author adell
 */

class FavouritesAdapter : RecyclerView.Adapter<FavouritesAdapter.FavouritesViewHolder>() {

    // lambda function that will be used to handle the click event on the favourite meal item in the RecyclerView
    lateinit var onFavouriteMealClicked: ((Meal) -> Unit) // This function will be called when a favourite meal is clicked

    private val diffUtil = object :
        DiffUtil.ItemCallback<Meal>() { // DiffUtil class to compare the old and new list of meals to determine which items have been added, removed, or changed
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean { // check if the items are the same (same id)
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean { // check if the contents of the items are the same (same object instance)
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(
        this,
        diffUtil
    ) // AsyncListDiffer class to handle the list of meals asynchronously

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavouritesViewHolder { // This function will be called to create a new ViewHolder object instance and return it to the RecyclerView when it needs a new item view to display.
        // Initialize the view binding object instance using the layout file name
        return FavouritesViewHolder(
            CategoryMealItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: FavouritesViewHolder,
        position: Int
    ) { // This function will be called to bind the data to the view holder object instance
        val meal = differ.currentList[position] // Get the meal at the current position
        Glide.with(holder.itemView) // holder.itemView is the root view of the item view in the RecyclerView (category_meal_item.xml)
            .load(meal.thumbUrl) // Load the image from the URL
            .into(holder.binding.ivMeal) // Set the image to the ImageView

        holder.binding.tvMealTitle.text = meal.name // Set the name of the meal to the TextView

        holder.itemView.setOnClickListener { // Set the click listener on the root view of the item view in the RecyclerView (category_meal_item.xml)
            onFavouriteMealClicked(meal) // Call the lambda function to handle the click event on the favourite meal item
        }
    }

    override fun getItemCount(): Int =
        differ.currentList.size // This function will return the number of items in the list

    // ViewHolder class that takes in the view binding object instance of the category_meal_item.xml layout file and extends the RecyclerView.ViewHolder class to hold the view binding object instance
    inner class FavouritesViewHolder(val binding: CategoryMealItemBinding) :
        RecyclerView.ViewHolder(binding.root) // ViewHolder class for the FavouritesAdapter class to hold the view binding object instance of the category_meal_item.xml layout file
}