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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(javaClass.simpleName, "onViewCreated")
        val binding = FragmentRvBinding.bind(view)
        viewModel.setTitle("Favorites")
        viewModel.hideActionBarFavorites()
        // XXX Write me
    }
}