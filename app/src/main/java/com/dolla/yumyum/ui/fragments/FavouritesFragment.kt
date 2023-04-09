package com.dolla.yumyum.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.dolla.yumyum.adapters.FavouritesAndSearchAdapter
import com.dolla.yumyum.data.pojo.Meal
import com.dolla.yumyum.databinding.FragmentFavouritesBinding
import com.dolla.yumyum.ui.activites.MainActivity
import com.dolla.yumyum.ui.activites.MealActivity
import com.dolla.yumyum.ui.fragments.HomeFragment.Companion.MEAL_ID
import com.dolla.yumyum.ui.fragments.HomeFragment.Companion.MEAL_NAME
import com.dolla.yumyum.ui.fragments.HomeFragment.Companion.MEAL_THUMB
import com.dolla.yumyum.viewModel.HomeViewModel
import com.google.android.material.snackbar.Snackbar
import java.util.*

class FavouritesFragment : Fragment() {

    private lateinit var binding: FragmentFavouritesBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var favouritesAndSearchAdapter: FavouritesAndSearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) { // This method is called when the fragment is first created
        super.onCreate(savedInstanceState)

        // Get the HomeViewModel instance from the MainActivity
        viewModel = (activity as MainActivity).viewModel

        // Initialize the favouritesAdapter object instance
        favouritesAndSearchAdapter = FavouritesAndSearchAdapter()
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
        observeNoFavouritesState() // Observe the no favourites state LiveData
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
                        favouritesAndSearchAdapter.differ.currentList.toMutableList() // Get the current list of meals

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

                    favouritesAndSearchAdapter.differ.submitList(oldList) // Submit the new list to the differ
                    favouritesAndSearchAdapter.notifyItemMoved(
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
                        favouritesAndSearchAdapter.differ.currentList[viewHolder.adapterPosition] // Get the meal at the swiped position
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

    private fun prepareFavouritesRecyclerView() { // Prepare the RecyclerView for the favourites
        binding.rvFavourites.apply {
            layoutManager = GridLayoutManager(
                activity,
                2,
                GridLayoutManager.VERTICAL,
                false
            ) // Set the layout manager for the RecyclerView to a GridLayoutManager
            adapter = favouritesAndSearchAdapter // Set the adapter for the RecyclerView
            setHasFixedSize(true) // Set the hasFixedSize property to true (improves performance)
        }
    }

    private fun observeFavourites() { // Observe the favourites LiveData in the HomeViewModel
        viewModel.favouriteMealsLiveData.observe(viewLifecycleOwner) { meals ->
            if (meals.isEmpty()) { // If there are no favourites
                viewModel.setNoFavouriteMeals() // Set the favourite meals state to no favourites
            } else { // If there are favourites
                viewModel.setFavouriteMeals() // Set the favourite meals state to favourites
            }
            favouritesAndSearchAdapter.differ.submitList(meals) // Submit the list of meals to the differ
        }
    }

    private fun observeNoFavouritesState() { // Observe the no favourites state LiveData in the HomeViewModel
        viewModel.noFavouriteMealsLiveData.observe(viewLifecycleOwner) { noFavourites ->
            if (noFavourites) { // If there are no favourites
                binding.tvNoFavourites.visibility = View.VISIBLE // Show the no favourites text
//                binding.rvFavourites.visibility = View.GONE // Hide the RecyclerView
            } else { // If there are favourites
                binding.tvNoFavourites.visibility = View.GONE // Hide the no favourites text
//                binding.rvFavourites.visibility = View.VISIBLE // Show the RecyclerView
            }
        }
    }

    private fun onFavouriteMealClick() { // Set the on click listener for the favourite meals
        favouritesAndSearchAdapter.onFavouriteAndSearchMealClicked = { favouriteMeal ->
            val intent = Intent(
                activity,
                MealActivity::class.java
            ) // Create an intent to start the MealActivity
            intent.apply {
                putExtra(
                    MEAL_ID,
                    favouriteMeal.id
                ) // Put the meal ID in the intent extras
                putExtra(
                    MEAL_NAME,
                    favouriteMeal.name
                ) // Put the meal name in the intent extras
                putExtra(
                    MEAL_THUMB,
                    favouriteMeal.thumbUrl
                ) // Put the meal image URL in the intent extras
            }
            startActivity(intent) // Start the MealActivity
        }
    }
}

