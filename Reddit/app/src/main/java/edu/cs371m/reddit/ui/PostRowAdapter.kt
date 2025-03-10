package edu.cs371m.reddit.ui

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import edu.cs371m.reddit.R
import edu.cs371m.reddit.api.RedditPost
import edu.cs371m.reddit.databinding.RowPostBinding
import edu.cs371m.reddit.glide.Glide
//import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import edu.cs371m.reddit.glide.Glide.glideOptions
import edu.cs371m.reddit.glide.Glide.height
import edu.cs371m.reddit.glide.Glide.width


// https://developer.android.com/reference/androidx/recyclerview/widget/ListAdapter
// Slick adapter that provides submitList, so you don't worry about how to update
// the list, you just submit a new one when you want to change the list and the
// Diff class computes the smallest set of changes that need to happen.
// NB: Both the old and new lists must both be in memory at the same time.
// So you can copy the old list, change it into a new list, then submit the new list.
//
// You can call adapterPosition to get the index of the selected item
class PostRowAdapter(private val viewModel: MainViewModel,
                     private val navigateToOnePost: (RedditPost)->Unit )
    : ListAdapter<RedditPost, PostRowAdapter.VH>(RedditDiff()) {
    class RedditDiff : DiffUtil.ItemCallback<RedditPost>() {
        override fun areItemsTheSame(oldItem: RedditPost, newItem: RedditPost): Boolean {
            return oldItem.key == newItem.key
        }
        override fun areContentsTheSame(oldItem: RedditPost, newItem: RedditPost): Boolean {
            return RedditPost.spannableStringsEqual(oldItem.title, newItem.title) &&
                    RedditPost.spannableStringsEqual(oldItem.selfText, newItem.selfText) &&
                    RedditPost.spannableStringsEqual(oldItem.publicDescription, newItem.publicDescription) &&
                    RedditPost.spannableStringsEqual(oldItem.displayName, newItem.displayName)

        }
    }
    //Correctly implemented ViewHolder
    inner class VH(val binding: RowPostBinding) : RecyclerView.ViewHolder(binding.root)

    //Correctly implemented onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = RowPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    //Correctly implemented onBindViewHolder
    override fun onBindViewHolder(holder: VH, position: Int) {

        val post = getItem(position) // Get RedditPost object
        val binding = holder.binding  // Get binding from ViewHolder

        binding.rowFav.setOnClickListener {

            viewModel.toggleFavorite(post)

            val isFavorite = viewModel.observeFavorites().value?.contains(post) == true
            binding.rowFav.setImageResource(
                if (isFavorite) R.drawable.ic_favorite_black_24dp else R.drawable.ic_favorite_border_black_24dp
            )
        }

        //Set title
        binding.title.text = post.title

        //binding.selfText.text = post.selfText ?: ""

        binding.score.text = post.score.toString()

        binding.comments.text = post.commentCount.toString()


        //Set selfText or hide it if empty
        if (post.selfText.isNullOrEmpty()) {
            binding.selfText.visibility = View.GONE
        } else {
            binding.selfText.text = post.selfText
            binding.selfText.visibility = View.VISIBLE
        }


        //Load image with Glide
        Glide.glideFetch(
            post.imageURL ?: "",
            post.thumbnailURL ?: "",
            binding.image  
        )

        //Click listener to open OnePostFragment
        binding.root.setOnClickListener {
            navigateToOnePost(post)
        }
    }

}

