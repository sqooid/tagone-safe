package com.example.tagone.detailedinformation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tagone.util.DisplayModel

class DetailedInformationViewModel(val post: DisplayModel) : ViewModel() {

    /**
     * viewModel factory for viewModel
     */
    class ViewModelFactory(private val post: DisplayModel) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetailedInformationViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DetailedInformationViewModel(post) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}