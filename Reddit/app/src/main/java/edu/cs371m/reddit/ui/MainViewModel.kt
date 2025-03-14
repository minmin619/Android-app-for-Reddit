package edu.cs371m.reddit.ui


import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.cs371m.reddit.api.RedditApi
import edu.cs371m.reddit.api.RedditPost
import edu.cs371m.reddit.api.RedditPostRepository
import edu.cs371m.reddit.databinding.ActionBarBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.lifecycle.map

// XXX Much to write
class MainViewModel : ViewModel() {
    private var title = MutableLiveData<String>()
    private var searchTerm = MutableLiveData<String>()
    private var subreddit = MutableLiveData<String>().apply {
        value = "aww"
    }
    private var actionBarBinding : ActionBarBinding? = null

    // XXX Write me, api, repository, favorites
    // API and repository
    private val redditApi = RedditApi.create()
    private val repository = RedditPostRepository(redditApi)

    // Favorites List
    private val favorites = MutableLiveData<Set<RedditPost>>(emptySet())



    // netSubreddits fetches the list of subreddits
    // We only do this once, so technically it does not need to be
    // MutableLiveData, or even really LiveData.  But maybe in the future
    // we will refetch it.
    private var netSubreddits = MutableLiveData<List<RedditPost>>().apply{
        // XXX Write me, viewModelScope.launch getSubreddits()
        viewModelScope.launch(Dispatchers.IO) {
            val subreddits = repository.getSubreddits()
            postValue(subreddits)
        }

    }
    // netPosts fetches the posts for the current subreddit, when that
    // changes
    private var netPosts = MediatorLiveData<List<RedditPost>>().apply {
        addSource(subreddit) { subreddit: String ->
            Log.d("repoPosts", subreddit)
            // XXX Write me, viewModelScope.launch getPosts
            viewModelScope.launch(Dispatchers.IO) {
                val posts = repository.getPosts(subreddit)
                postValue(posts)
            }

        }
    }
    // XXX Write me MediatorLiveData searchSubreddit, searchFavorites
    // searchPosts
    // Search for matching subreddits
    private val searchSubreddits = MediatorLiveData<List<RedditPost>>().apply {
        addSource(netSubreddits) { filterSubreddits() }
        addSource(searchTerm) { filterSubreddits() }
    }

    private fun filterSubreddits() {
        val term = searchTerm.value.orEmpty()
        searchSubreddits.value = netSubreddits.value?.filter { it.displayName?.contains(term, true) == true }
    }

    // Search for matching posts
    private val searchPosts = MediatorLiveData<List<RedditPost>>().apply {
        addSource(netPosts) { filterPosts() }
        addSource(searchTerm) { filterPosts() }
    }
    private fun filterPosts() {
        val term = searchTerm.value.orEmpty()
        searchPosts.value = netPosts.value?.filter { it.title.contains(term, true) || it.selfText?.contains(term, true) == true }
    }

    // Search for favorites
    private val searchFavorites = MediatorLiveData<List<RedditPost>>().apply {
        addSource(favorites) { filterFavorites() }
        addSource(searchTerm) { filterFavorites() }
    }

    private fun filterFavorites() {
        val term = searchTerm.value.orEmpty()
        searchFavorites.value = favorites.value?.filter { it.title.contains(term, true) || it.selfText?.contains(term, true) == true }
    }



    // Looks pointless, but if LiveData is set up properly, it will fetch posts
    // from the network
    fun repoFetch() {
        val fetch = subreddit.value
        subreddit.value = fetch
    }

    fun observeTitle(): LiveData<String> {
        return title
    }
    fun setTitle(newTitle: String) {
        title.value = newTitle
    }

    fun updateTitle(subreddit: String) {
        title.value = "r/$subreddit"
    }

    fun observeSubreddit(): LiveData<String> {
        return subreddit
    }
    fun changeSubreddit(newSubreddit: String) {
        subreddit.value = newSubreddit
        updateTitle(newSubreddit)
    }

    fun observeSearch(): LiveData<String> = searchTerm
    fun setSearchTerm(term: String) {
        searchTerm.value = term
    }

    fun observePosts(): LiveData<List<RedditPost>> = searchPosts
    fun observeSubreddits(): LiveData<List<RedditPost>> = searchSubreddits
    fun observeFavorites(): LiveData<List<RedditPost>> {
        return favorites.map { it.toList() }
    }
    fun observeDisplayedPosts(): LiveData<List<RedditPost>> {
        val displayedPosts = MediatorLiveData<List<RedditPost>>()

        val updatePosts = {
            val favoriteMode = isFavoriteMode.value ?: false
            displayedPosts.value = if (favoriteMode) {
                favorites.value?.toList() ?: emptyList()  // 只顯示收藏的貼文
            } else {
                searchPosts.value ?: emptyList()  // 顯示所有貼文
            }
        }

        displayedPosts.addSource(isFavoriteMode) { updatePosts() }
        displayedPosts.addSource(favorites) { updatePosts() }
        displayedPosts.addSource(searchPosts) { updatePosts() }

        return displayedPosts
    }


    // ONLY call this from OnePostFragment, otherwise you will have problems.
    fun observeSearchPost(post: RedditPost): LiveData<RedditPost> {

            val searchPost = MediatorLiveData<RedditPost>().apply {

                // XXX Write me
                value = post
                addSource(searchTerm) { term ->
                    val newPost = post.copy()
                    newPost.searchFor(term)
                    postValue(newPost)
                }
            }
            return searchPost
    }

    /////////////////////////
    // Action bar
    fun initActionBarBinding(it: ActionBarBinding) {
        // XXX Write me, one liner
        actionBarBinding = it
    }
    fun hideActionBarFavorites() {
        // XXX Write me, one liner
        actionBarBinding?.actionFavorite?.visibility = View.GONE
    }
    fun showActionBarFavorites() {

        // XXX Write me, one liner
        actionBarBinding?.actionFavorite?.visibility = View.VISIBLE

    }

    // XXX Write me, set, observe, deal with favorites
    fun toggleFavorite(post: RedditPost) {
        val currentFavorites = favorites.value ?: emptySet()
        val newFavorites = if (currentFavorites.contains(post)) {
            currentFavorites - post
        } else {
            currentFavorites + post
        }
        favorites.value = newFavorites
    }

    private val isFavoriteMode = MutableLiveData(false)


    fun toggleFavoriteMode() {
        isFavoriteMode.value = !(isFavoriteMode.value ?: false)
        if (isFavoriteMode.value == true) {
            setTitle("Favorites")
        } else {
            setTitle("r/${subreddit.value}")
        }
    }

    // ✅ 監聽 Favorite 狀態
    fun observeFavoritesMode(): LiveData<Boolean> = isFavoriteMode

}