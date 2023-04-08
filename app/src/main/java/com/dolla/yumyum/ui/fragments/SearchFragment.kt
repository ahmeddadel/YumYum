package com.dolla.yumyum.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dolla.yumyum.adapters.FavouritesAndSearchAdapter
import com.dolla.yumyum.databinding.FragmentSearchBinding
import com.dolla.yumyum.ui.activites.MainActivity
import com.dolla.yumyum.viewModel.HomeViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var searchAdapter: FavouritesAndSearchAdapter
    private var searchJob: Job? = null // Create a Job object

    override fun onCreate(savedInstanceState: Bundle?) { // This method is called when the fragment is first created
        super.onCreate(savedInstanceState)

        // Get the HomeViewModel instance from the MainActivity
        viewModel = (activity as MainActivity).viewModel

        // Initialize the searchAdapter object instance
        searchAdapter = FavouritesAndSearchAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View { // This method is called to have the fragment instantiate its user interface view
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) { // This method is called after onCreateView when the fragment's view hierarchy is created and ready to be used
        super.onViewCreated(view, savedInstanceState)

        binding.etSearch.requestFocus() // Request focus for the EditText
        showKeyboard(binding.etSearch) // Show the keyboard
        prepareSearchRecyclerView() // Prepare the RecyclerView for the search
        onSearchTextChange() // Set the on text change listener for the search EditText
        onSearchClick() // Set the on click listener for the search meals
        onKeyboardSearchClick() // Set the on click listener for the keyboard search button
        observeSearchedMeals() // Observe the searched meals LiveData
    }

    private fun prepareSearchRecyclerView() { // Prepare the RecyclerView for the search
        binding.rvSearch.apply {
            layoutManager = GridLayoutManager(
                activity,
                2,
                LinearLayoutManager.VERTICAL,
                false
            ) // Set the layout manager for the RecyclerView
            adapter = searchAdapter // Set the adapter for the RecyclerView
            setHasFixedSize(true) // Set the hasFixedSize property to true (improves performance)
        }
    }

    private fun onSearchTextChange() { // Set the on text change listener for the search EditText
        binding.etSearch.addTextChangedListener { searchQuery ->
            searchJob?.cancel() // Cancel the previous job
            searchJob = lifecycleScope.launch { // Create a new job
                delay(500L) // Delay the job for 500 milliseconds (to avoid unnecessary network calls)
                viewModel.searchMealByName(searchQuery.toString()) // Call the searchMeals method of the HomeViewModel
            }
        }
    }

    private fun onSearchClick() { // Set the on click listener for the search button
        binding.ivSearchButton.setOnClickListener {
            val searchQuery = binding.etSearch.text.toString() // Get the text from the EditText
            if (searchQuery.isNotEmpty()) // If the search query is not empty
                viewModel.searchMealByName(searchQuery) // Call the searchMeals method of the HomeViewModel
            hideKeyboard() // Hide the keyboard
        }
    }

    private fun onKeyboardSearchClick() { // Set the on click listener for the search button
        binding.etSearch.setOnEditorActionListener { _, _, _ ->
            val searchQuery = binding.etSearch.text.toString() // Get the text from the EditText
            if (searchQuery.isNotEmpty()) // If the search query is not empty
                viewModel.searchMealByName(searchQuery) // Call the searchMeals method of the HomeViewModel
            hideKeyboard() // Hide the keyboard
            true
        }
    }

    private fun observeSearchedMeals() { // Observe the searched meals LiveData
        viewModel.searchedMealLiveData.observe(viewLifecycleOwner) { searchedMeals ->
            searchAdapter.differ.submitList(searchedMeals) // Submit the list of searched meals to the adapter
        }
    }

    private fun Fragment.hideKeyboard() { // Hide the keyboard
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager // Get the InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            requireView().windowToken,
            0
        ) // Hide the keyboard
    }

    private fun Fragment.showKeyboard(view: View) {
        if (view.requestFocus()) {
            val inputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}