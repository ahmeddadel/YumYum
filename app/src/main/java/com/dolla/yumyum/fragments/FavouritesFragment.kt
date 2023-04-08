package com.dolla.yumyum.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.dolla.yumyum.activites.MainActivity
import com.dolla.yumyum.activites.MealActivity
import com.dolla.yumyum.adapters.FavouritesAdapter
import com.dolla.yumyum.databinding.FragmentFavouritesBinding
import com.dolla.yumyum.pojo.Meal
import com.dolla.yumyum.viewModel.HomeViewModel
import com.google.android.material.snackbar.Snackbar
import java.util.*

class FavouritesFragment : Fragment() {

    private lateinit var binding: FragmentFavouritesBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var favouritesAdapter: FavouritesAdapter

    override fun onCreate(savedInstanceState: Bundle?) { // This method is called when the fragment is first created
        super.onCreate(savedInstanceState)

        // Get the HomeViewModel instance from the MainActivity
        viewModel = (activity as MainActivity).viewModel

        // Initialize the favouritesAdapter object instance
        favouritesAdapter = FavouritesAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View { // This method is called to have the fragment instantiate its user interface view
        // Inflate the layout for this fragment
        binding = FragmentFavouritesBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) { // This method is called after onCreateView when the fragment's view hierarchy is created and ready to be used
        super.onViewCreated(view, savedInstanceState)

        prepareFavouritesRecyclerView() // Prepare the RecyclerView for the favourites
        observeFavourites() // Observe the favourites LiveData
        onFavouriteMealClick() // Set the on click listener for the favourite meals

        val itemTouchHelper = ItemTouchHelper( // Create an ItemTouchHelper object
            object : ItemTouchHelper.SimpleCallback( // Create a SimpleCallback object
                ItemTouchHelper.UP or ItemTouchHelper.DOWN, // Set the drag directions
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT // Set the swipe directions
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean { // This method is called when an item is moved in the RecyclerView (dragged)

                    val fromPosition = viewHolder.adapterPosition // Get the from position
                    val toPosition = target.adapterPosition // Get the to position

                    val oldList: MutableList<Meal> =
                        favouritesAdapter.differ.currentList.toMutableList() // Get the current list of meals

                    // Swap the items in the list based on the from and to positions
                    if (fromPosition < toPosition) {
                        for (i in fromPosition until toPosition - 2) {
                            Collections.swap(oldList, i, i + 1)
                        }
                    } else {
                        for (i in fromPosition downTo toPosition + 2) {
                            Collections.swap(oldList, i, i - 1)
                        }
                    }

                    favouritesAdapter.differ.submitList(oldList) // Submit the new list to the differ
                    favouritesAdapter.notifyItemMoved(
                        fromPosition,
                        toPosition
                    ) // Notify the adapter that the items have been moved

                    return true // Return true
                }

                override fun onSwiped(
                    viewHolder: RecyclerView.ViewHolder,
                    direction: Int
                ) { // This method is called when an item is swiped in the RecyclerView (swiped)
                    val meal =
                        favouritesAdapter.differ.currentList[viewHolder.adapterPosition] // Get the meal at the swiped position
                    viewModel.deleteMealFromDb(meal) // Delete the meal from the database

                    Snackbar.make( // Show a snackbar
                        view,
                        "Successfully removed ${meal.name} from favourites",
                        Snackbar.LENGTH_LONG
                    ).apply {
                        setAction("Undo") {
                            viewModel.insertMealIntoDb(meal)
                        }
                        show()
                    }
                }
            }
        )

        itemTouchHelper.attachToRecyclerView(binding.rvFavourites) // Attach the ItemTouchHelper to the RecyclerView
    }

    private fun prepareFavouritesRecyclerView() {
        binding.rvFavourites.apply {
            layoutManager = GridLayoutManager(
                activity,
                2,
                GridLayoutManager.VERTICAL,
                false
            ) // Set the layout manager for the RecyclerView to a GridLayoutManager
            adapter = favouritesAdapter // Set the adapter for the RecyclerView
            setHasFixedSize(true) // Set the hasFixedSize property to true (improves performance)
        }
    }

    private fun observeFavourites() { // Observe the favourites LiveData in the HomeViewModel
        viewModel.favouriteMealsLiveData.observe(viewLifecycleOwner) { meals ->
            favouritesAdapter.differ.submitList(meals) // Submit the list of meals to the differ
        }
    }

    private fun onFavouriteMealClick() { // Set the on click listener for the favourite meals
        favouritesAdapter.onFavouriteMealClicked = { favouriteMeal ->
            val intent = Intent(
                activity,
                MealActivity::class.java
            ) // Create an intent to start the MealActivity
            intent.apply {
                putExtra(
                    HomeFragment.MEAL_ID,
                    favouriteMeal.id
                ) // Put the meal ID in the intent extras
                putExtra(
                    HomeFragment.MEAL_NAME,
                    favouriteMeal.name
                ) // Put the meal name in the intent extras
                putExtra(
                    HomeFragment.MEAL_THUMB,
                    favouriteMeal.thumbUrl
                ) // Put the meal image URL in the intent extras
            }
            startActivity(intent) // Start the MealActivity
        }
    }
}

