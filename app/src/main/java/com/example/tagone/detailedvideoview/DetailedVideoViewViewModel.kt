package com.example.tagone.detailedview

import android.app.Application
import androidx.lifecycle.*
import com.example.tagone.database.getDatabase
import com.example.tagone.util.DisplayModel
import com.example.tagone.util.PostsRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailedVideoViewViewModel(
    application: Application,
    private val post: DisplayModel,
    private val postNumber: Int
) :
    ViewModel() {

    /**
     * Repository
     */
    private val repository = PostsRepository(getDatabase(application))

    /**
     * Live data variables
     */
    private val _linkedTag = MutableLiveData<String>()
    val linkedTag: LiveData<String>
        get() = _linkedTag

    lateinit var isFavourited: LiveData<Boolean>

    /**
     * Function called from fragment by on-click listener to link tag cards to tag search
     */
    fun searchLinkedTag(tag: String) {
        _linkedTag.value = tag
    }

    fun doneSearchingLinkedTag() {
        _linkedTag.value = null
    }

    fun watchFavouriteStatus() {
        if (post.id != null) {
            isFavourited = repository.isFavourited(post.id)
        }
    }

    fun toggleFavourite() {
        if (isFavourited.value!!) {
            removeFromFavourites(post)
        } else {
            addToFavourites(post)
        }
    }


    /**
     * ViewModel factory for this viewModel
     */
    class ViewModelFactory(
        private val application: Application,
        private val post: DisplayModel,
        private val postNumber: Int
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetailedVideoViewViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DetailedVideoViewViewModel(application, post, postNumber) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

    private fun addToFavourites(post: DisplayModel) {
        viewModelScope.launch {
            repository.addToFavourites(post)
        }
    }

    private fun removeFromFavourites(post: DisplayModel) {
        viewModelScope.launch {
            repository.removeFromFavourites(post)
        }
    }
}


