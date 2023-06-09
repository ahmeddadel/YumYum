package com.dolla.yumyum.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dolla.yumyum.data.pojo.Category
import com.dolla.yumyum.databinding.CategoryItemBinding

/**
 * @created 02/04/2023 - 11:17 PM
 * @project YumYum
 * @author adell
 */

class CategoriesAdapter :
    RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() { // Adapter class for the RecyclerView in the HomeFragment

    // lambda function that will be used to handle the click event on the category item in the RecyclerView
    lateinit var onCategoryClicked: (Category) -> Unit // This function will be called when a category is clicked
    private var categoriesList = ArrayList<Category>() // This list will hold the list of categories

    fun setCategoriesList(categoriesList: ArrayList<Category>) { // This function will be used to set the list of categories to the adapter and notify the adapter that the data set has changed
        this.categoriesList = categoriesList
        notifyDataSetChanged() // Notify the adapter that the data set has changed
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoriesViewHolder { // This function will be called to create a new ViewHolder object instance and return it to the RecyclerView when it needs a new item view to display.
        // Initialize the view binding object instance using the layout file name
        return CategoriesViewHolder(
            CategoryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: CategoriesViewHolder,
        position: Int
    ) { // This function will be called to bind the data to the view holder object instance

        // set animation to the view holder item view (card view) when it is created for the first time only
        holder.itemView.startAnimation(
            AnimationUtils.loadAnimation(
                holder.itemView.context,
                android.R.anim.slide_in_left
            )
        )

        Glide.with(holder.itemView) // holder.itemView is the root view of the item view in the RecyclerView (popular_items.xml)
            .load(categoriesList[position].thumbUrl) // Load the image from the URL
            .into(holder.binding.ivCategory) // Set the image to the ImageView

        holder.binding.tvCategoryName.text =
            categoriesList[position].name // Set the category name to the TextView

        holder.itemView.setOnClickListener { // Set the click listener on the root view of the item view in the RecyclerView (popular_items.xml)
            onCategoryClicked(categoriesList[position]) // Call the lambda function to handle the click event on the category item
        }
    }

    override fun getItemCount(): Int =
        categoriesList.size // This function will return the number of items in the list

    // ViewHolder class that takes in the view binding object instance of the popular_items.xml layout file and extends the RecyclerView.ViewHolder class to hold the view binding object instance
    inner class CategoriesViewHolder(val binding: CategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) // ViewHolder class that takes in the view binding object instance of the popular_items.xml layout file and extends the RecyclerView.ViewHolder class to hold the view binding object instance

}

