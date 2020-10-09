package com.example.tagone.detailedview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tagone.util.DisplayModel

class DetailedViewViewModel(private val post: DisplayModel, private val postNumber: Int) : ViewModel() {



    /**
     * ViewModel factory for this viewModel
     */
    class ViewModelFactory(private val post: DisplayModel, private val postNumber: Int) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetailedViewViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DetailedViewViewModel(post, postNumber) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}