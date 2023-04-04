package com.dolla.yumyum.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.dolla.yumyum.activites.MainActivity
import com.dolla.yumyum.adapters.FavouritesAdapter
import com.dolla.yumyum.databinding.FragmentFavouritesBinding
import com.dolla.yumyum.viewModel.HomeViewModel

class FavouritesFragment : Fragment() {

    private lateinit var binding: FragmentFavouritesBinding
    private lateinit var favouritesViewModel: HomeViewModel
    private lateinit var favouritesAdapter: FavouritesAdapter

    override fun onCreate(savedInstanceState: Bundle?) { // This method is called when the fragment is first created
        super.onCreate(savedInstanceState)

        // Get the HomeViewModel instance from the MainActivity
        favouritesViewModel = (activity as MainActivity).viewModel

        // Initialize the favouritesAdapter object instance
        favouritesAdapter = FavouritesAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View { // This method is called to have the fragment instantiate its user interface view
        // Inflate the layout for this fragment
        binding = FragmentFavouritesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) { // This method is called after onCreateView when the fragment's view hierarchy is created and ready to be used
        super.onViewCreated(view, savedInstanceState)

        prepareFavouritesRecyclerView() // Prepare the RecyclerView for the favourites
        observeFavourites() // Observe the favourites LiveData
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
        favouritesViewModel.favoriteMealsLiveData.observe(viewLifecycleOwner) { meals ->
            favouritesAdapter.differ.submitList(meals) // Submit the list of meals to the differ
        }
    }
}