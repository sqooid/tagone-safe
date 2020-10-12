package com.example.tagone.detailedview

import android.app.Application
import android.net.Uri
import android.view.View
import android.widget.ProgressBar
import android.widget.VideoView
import androidx.core.net.toUri
import androidx.lifecycle.*
import com.example.tagone.database.getDatabase
import com.example.tagone.util.DisplayModel
import com.example.tagone.util.PostsRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailedViewViewModel(
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
     * General variables for use in UI
     */


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
       if (post.fileUrl != null) {
           isFavourited = repository.isFavourited(post.fileUrl)
       }
    }

    fun toggleFavourite() {
        if (isFavourited.value!!) {
            removeFromFavourites(post)
        } else {
            addToFavourites(post)
        }
    }

    fun getVideoUri(): Uri {
        return if (post.fileUrl != null) {
            post.fileUrl.toUri().buildUpon().scheme("https").build()
        } else {
            Uri.EMPTY
        }

    }

    fun setVideoViewData(videoView: VideoView, progressBar: ProgressBar) {
        with (videoView) {
            setVideoURI(getVideoUri())
            canPause()
            canSeekBackward()
            canSeekForward()
            setOnPreparedListener {
                it.setVolume(1F, 1F)
                it.isLooping = true
                progressBar.visibility = View.GONE
            }
            start()
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


