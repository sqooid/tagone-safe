package com.example.tagone.tagsearch

import android.app.Application
import android.util.Log
import android.view.WindowManager
import androidx.lifecycle.*
import com.example.tagone.database.getDatabase
import com.example.tagone.network.DanbooruApi
import com.example.tagone.network.toDisplayModel
import com.example.tagone.util.DisplayModel
import com.example.tagone.util.PostsRepository
import kotlinx.coroutines.launch

class TagSearchViewModel(application: Application) : ViewModel() {

    /**
     * Repository
     */
    private val repository = PostsRepository(getDatabase(application))

    val posts = repository.searchList

    private val _postSuccess = MutableLiveData<Boolean>()
    val postSuccess: LiveData<Boolean>
        get() = _postSuccess

    private val _searchParameters = MutableLiveData<String>()
    val searchParameters: LiveData<String>
        get() = _searchParameters

    private var currentTags = ""
    private val postsPerPage = 100
    private var pageNumber = 1


    /**
     * Function that is run when user hits search button. Resets posts
     */
    fun doInitialSearchWithTags(tags: String) {
        _searchParameters.value = tags
        currentTags = tags
        pageNumber = 0
        viewModelScope.launch {
            repository.getPostsFromNetwork(tags, postsPerPage, pageNumber)
            _postSuccess.value = true
            if (posts.value.isNullOrEmpty()) {
                _postSuccess.value = false
            }
        }
    }

    /**
     * Function to call when the next page of posts is requestd
     */
    fun getMorePosts() {
        pageNumber++
        viewModelScope.launch {
            repository.addPostsFromNetwork(currentTags, postsPerPage, pageNumber)
        }
    }

    /**
     * ViewModel factory for viewModel. Currently not in use
     */
    class viewModelFactory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TagSearchViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return TagSearchViewModel(application) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

}
