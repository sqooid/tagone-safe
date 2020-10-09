package com.example.tagone.util

import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.tagone.R
import com.example.tagone.tagsearch.TagSearchAdapter

/**
 * Updating Recycler View
 */
@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<DisplayModel>?) {
    val adapter = recyclerView.adapter as TagSearchAdapter
    adapter.submitList(data)
}

/**
 * Loading image from URL
 */
@BindingAdapter("loadedImage")
fun displayImage(imageView: ImageView, imageUrl: String?) {
    imageUrl?.let {
        val imageUri = imageUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imageView.context)
            .load(imageUri)
            .placeholder(R.drawable.ic_baseline_smoke_free_24)
            .dontAnimate()
            .apply(RequestOptions().override(imageView.layoutParams.width,0))
            .into(imageView)
    }
}

/**
 * Random binding adapter for basic textView
 */
@BindingAdapter("loadedText")
fun displayText(textView: TextView, text: String?) {
    text?.let {
        textView.text = text
    }
}