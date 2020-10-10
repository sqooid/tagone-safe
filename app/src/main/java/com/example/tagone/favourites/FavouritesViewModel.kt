package com.example.tagone.favourites

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tagone.database.getDatabase
import com.example.tagone.tagsearch.TagSearchViewModel
import com.example.tagone.util.PostsRepository

class FavouritesViewModel(application: Application) : ViewModel() {

    val repository = PostsRepository(getDatabase(application))
    val posts = repository.favouritesList

    /**
     * ViewModel factory for Favourites viewModel
     */
    class ViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FavouritesViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FavouritesViewModel(application) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}