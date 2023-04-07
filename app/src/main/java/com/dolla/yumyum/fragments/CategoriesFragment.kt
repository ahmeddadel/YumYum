package com.dolla.yumyum.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.dolla.yumyum.activites.CategoryMealsActivity
import com.dolla.yumyum.activites.MainActivity
import com.dolla.yumyum.adapters.CategoriesAdapter
import com.dolla.yumyum.databinding.FragmentCategoriesBinding
import com.dolla.yumyum.pojo.Category
import com.dolla.yumyum.viewModel.HomeViewModel

class CategoriesFragment : Fragment() {

    private lateinit var binding: FragmentCategoriesBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var categoriesAdapter: CategoriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) { // This method is called when the fragment is first created
        super.onCreate(savedInstanceState)

        // Get the HomeViewModel instance from the MainActivity
        viewModel = (activity as MainActivity).viewModel

        // Initialize the categoriesAdapter object instance
        categoriesAdapter = CategoriesAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View { // This method is called to have the fragment instantiate its user interface view
        // Inflate the layout for this fragment
        binding = FragmentCategoriesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) { // This method is called after onCreateView when the fragment's view hierarchy is created and ready to be used
        super.onViewCreated(view, savedInstanceState)

        prepareCategoriesRecyclerView() // Prepare the RecyclerView for the favourites
        observeCategories() // Observe the categories LiveData
        onCategoryClick() // Set the on click listener for the categories
    }

    private fun prepareCategoriesRecyclerView() { // Prepare the RecyclerView for the categories
        binding.rvCategories.apply { // Apply the following to the RecyclerView
            layoutManager = GridLayoutManager(
                activity,
                3,
                GridLayoutManager.VERTICAL,
                false
            ) // Set the layout manager for the RecyclerView to a GridLayoutManager
            adapter = categoriesAdapter // Set the adapter for the RecyclerView
            setHasFixedSize(true) // Set the hasFixedSize property to true (improve performance)
        }
    }

    private fun observeCategories() { // Observe the categories LiveData in the HomeViewModel
        viewModel.categoriesLiveData.observe(viewLifecycleOwner) { categories -> // Observe the categories LiveData
            categoriesAdapter.setCategoriesList(categories as ArrayList<Category>) // Submit the list of categories to the adapter
        }
    }

    private fun onCategoryClick() { // Set the onClickListener for the categories
        categoriesAdapter.onCategoryClicked = { category ->
            val intent = Intent(
                activity,
                CategoryMealsActivity::class.java
            ) // Create an intent to start the CategoryActivity
            intent.putExtra(
                HomeFragment.CATEGORY_NAME,
                category.name
            ) // Put the category in the intent extras
            startActivity(intent) // Start the CategoryActivity (pass the intent)
        }
    }
}