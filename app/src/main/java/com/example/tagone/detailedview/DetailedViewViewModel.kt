package com.example.tagone.detailedview

import android.app.Application
import androidx.lifecycle.*
import com.example.tagone.database.getDatabase
import com.example.tagone.util.DisplayModel
import com.example.tagone.util.PostsRepository
import kotlinx.coroutines.launch

class DetailedViewViewModel(
    application: Application,
    private val post: DisplayModel,
    private val postNumber: Int
) :
    ViewModel() {
    private val _linkedTag = MutableLiveData<String>()
    val linkedTag: LiveData<String>
        get() = _linkedTag

    private val _isFavourited = MutableLiveData<Boolean>()
    val isFavourited: LiveData<Boolean>
        get() = _isFavourited

    init {
        _isFavourited.value = post.localFavourite
    }

    /**
     * Database
     */
    private val repository = PostsRepository(getDatabase(application))

    /**
     * Function called from fragment by on-click listener to link tag cards to tag search
     */
    fun searchLinkedTag(tag: String) {
        _linkedTag.value = tag
    }

    fun doneSearchingLinkedTag() {
        _linkedTag.value = null
    }

    fun toggleFavourite() {
        _isFavourited.value?.let {
            if (!_isFavourited.value!!) {
                _isFavourited.value = true
                post.localFavourite = true
                addToFavourites(post)
            } else {
                removeFromFavourites(post)
                post.localFavourite = false
                _isFavourited.value = false
            }
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
            if (modelClass.isAssignableFrom(DetailedViewViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DetailedViewViewModel(application, post, postNumber) as T
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


