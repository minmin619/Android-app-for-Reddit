package edu.cs371m.reddit.ui

import android.os.Bundle
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs

import edu.cs371m.reddit.databinding.FragmentOnePostBinding
import edu.cs371m.reddit.ui.MainViewModel
import edu.cs371m.reddit.glide.Glide
import androidx.appcompat.app.AppCompatActivity


class OnePostFragment : Fragment() {
    private var _binding: FragmentOnePostBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private val args: OnePostFragmentArgs by navArgs()  // Corrected// Retrieves arguments passed by Navigation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnePostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val post = args.redditPost
        viewModel.setTitle("One Post")

        (activity as? AppCompatActivity)?.supportActionBar?.title = "One Post"
        binding.onePostSubreddit.text = "r/${post.subreddit}"
        // Display title
        binding.onePostTitle.text = post.title

        binding.onePostSelfText.text = HtmlCompat.fromHtml((post.selfText ?: "").toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)


        // Display selfText or hide it if empty
        if (!post.selfText.isNullOrEmpty()) {
            binding.onePostSelfText.text = post.selfText
            binding.onePostSelfText.visibility = View.VISIBLE
        } else {
            binding.onePostSelfText.text = SpannableString("No content available.")
            binding.onePostSelfText.visibility = View.VISIBLE
        }

        // Load image with Glide (if exists)
        Glide.glideFetch(
            post.imageURL ?: "",
            post.thumbnailURL ?: "",
            binding.onePostImage
        )

        viewModel.observeSearchPost(post).observe(viewLifecycleOwner) { updatedPost ->
            binding.onePostTitle.text = updatedPost.title

            if (!updatedPost.selfText.isNullOrEmpty()) {

                binding.onePostSelfText.text = updatedPost.selfText  // 確保 `selfText` 是 `SpannableString`
            } else {
                binding.onePostSelfText.text = SpannableString("")
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
