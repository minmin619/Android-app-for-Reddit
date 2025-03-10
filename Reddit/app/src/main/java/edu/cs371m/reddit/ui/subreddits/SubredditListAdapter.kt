package edu.cs371m.reddit.ui.subreddits

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.cs371m.reddit.api.RedditPost
import edu.cs371m.reddit.databinding.RowSubredditBinding
import edu.cs371m.reddit.glide.Glide
import edu.cs371m.reddit.ui.MainViewModel
import edu.cs371m.reddit.ui.PostRowAdapter

class SubredditListAdapter(
    private val viewModel: MainViewModel,
    private val navController: NavController
) : ListAdapter<RedditPost, SubredditListAdapter.VH>(PostRowAdapter.RedditDiff()) {

    // Correctly defined ViewHolder
    inner class VH(val binding: RowSubredditBinding) : RecyclerView.ViewHolder(binding.root)

    // Correctly placed onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = RowSubredditBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    // Correctly implemented onBindViewHolder
    override fun onBindViewHolder(holder: VH, position: Int) {
        val subreddit = getItem(position)
        val binding = holder.binding // Correct reference to ViewHolder binding

        // Set subreddit name
        binding.subRowHeading.text = subreddit.displayName

        // Set subreddit description
        binding.subRowDetails.text = subreddit.publicDescription ?: "No description available"

        //Corrected Glide usage
        Glide.glideFetch(
            subreddit.iconURL ?: "",
            subreddit.thumbnailURL ?: "",
            binding.subRowPic
        )

        // Click listener to change subreddit and navigate back
        binding.root.setOnClickListener {
            viewModel.changeSubreddit(subreddit.displayName.toString())
            navController.popBackStack() // Navigate back to HomeFragment
        }
    }
}
