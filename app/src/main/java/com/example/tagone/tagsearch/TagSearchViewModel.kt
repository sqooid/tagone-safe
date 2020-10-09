package com.example.tagone.tagsearch

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tagone.network.DanbooruApi
import com.example.tagone.network.toDisplayModel
import com.example.tagone.util.DisplayModel
import kotlinx.coroutines.launch

class TagSearchViewModel : ViewModel() {

    private val _posts = MutableLiveData<List<DisplayModel>>()
    val posts: LiveData<List<DisplayModel>>
        get() = _posts

    private val _postSuccess = MutableLiveData<Boolean>()
    val postSuccess: LiveData<Boolean>
        get() = _postSuccess

    private var currentTags = ""
    private val postsPerPage = 50
    private var pageNumber = 1

    /**
     * Function that is run when user hits search button. Resets posts
     */
    fun doInitialSearchWithTags(tags: String) {
        _posts.value = mutableListOf()
        currentTags = tags
        pageNumber = 0
        retrievePostsFromNetwork(tags, postsPerPage, pageNumber)
    }

    /**
     * Function to call when the next page of posts is requestd
     */
    fun getMorePosts() {
        pageNumber++
        retrievePostsFromNetwork(currentTags, postsPerPage, pageNumber)
        Log.i("Test", "Additional posts retrieved")
    }

    /**
     * Function that interacts with network to retrieve posts. Appends newly retrieved posts to existing (or empty) list of posts
     */
    private fun retrievePostsFromNetwork(tags: String, limit: Int, page: Int) {
        viewModelScope.launch {
            _posts.value = _posts.value?.plus(
                DanbooruApi.retrofitService.getPosts(tags, limit, page).toDisplayModel()
            )
            Log.i("Test", "Request made. ${_posts.value?.size} posts retrieved")
            _postSuccess.value = true
            if (_posts.value.isNullOrEmpty()) {
                _postSuccess.value = false
            }
        }
    }

    /**
     * ViewModel factory for viewModel. Currently not in use
     */
//    class viewModelFactory(val windowManager: WindowManager) : ViewModelProvider.Factory {
//        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//            if (modelClass.isAssignableFrom(TagSearchViewModel::class.java)) {
//                @Suppress("UNCHECKED_CAST")
//                return TagSearchViewModel(windowManager) as T
//            }
//            throw IllegalArgumentException("Unable to construct viewmodel")
//        }
//    }
}
