package edu.cs371m.reddit.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import edu.cs371m.reddit.R
import edu.cs371m.reddit.databinding.FragmentRvBinding

class Favorites: Fragment(R.layout.fragment_rv) {
    // XXX initialize viewModel
    private val viewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(javaClass.simpleName, "onViewCreated")
        val binding = FragmentRvBinding.bind(view)
        viewModel.setTitle("Favorites")
        viewModel.hideActionBarFavorites()
        // XXX Write me
        initAdapter(binding)

    }

    // Initialize the RecyclerView adapter and observe favorite posts
    private fun initAdapter(binding: FragmentRvBinding) {
        val postRowAdapter = PostRowAdapter(viewModel) { post ->
            // Clicking a favorite post navigates to OnePostFragment
            val direction = FavoritesDirections.actionFavoritesToOnePostFragment(post)
            findNavController().navigate(direction)
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = postRowAdapter
        }

        // Observe favorite posts and update the adapter
        viewModel.observeFavorites().observe(viewLifecycleOwner) { favorites ->
            postRowAdapter.submitList(favorites)
        }
    }
}