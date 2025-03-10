package edu.cs371m.reddit.ui.subreddits

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import edu.cs371m.reddit.R
import edu.cs371m.reddit.databinding.FragmentRvBinding
import edu.cs371m.reddit.ui.MainViewModel
import com.bumptech.glide.Glide

class Subreddits : Fragment(R.layout.fragment_rv) {
    // XXX initialize viewModel
    private val viewModel: MainViewModel by activityViewModels()

    // XXX Write me, onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(javaClass.simpleName, "onViewCreated")
        val binding = FragmentRvBinding.bind(view)

        // Set title to "Subreddits"
        viewModel.setTitle("Pick")
        initAdapter(binding)
    }

    // Initialize the RecyclerView adapter and observe the subreddit list
    private fun initAdapter(binding: FragmentRvBinding) {
        val subredditListAdapter = SubredditListAdapter(viewModel, findNavController())

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = subredditListAdapter
        }


        viewModel.observeSubreddits().observe(viewLifecycleOwner) { subreddits ->
            subredditListAdapter.submitList(subreddits)
        }
    }

}