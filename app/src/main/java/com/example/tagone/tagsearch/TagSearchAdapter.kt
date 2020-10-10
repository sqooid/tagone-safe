package com.example.tagone.tagsearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tagone.databinding.ImagePreviewBinding
import com.example.tagone.util.DisplayModel

class TagSearchAdapter(private val screenWidth: Int, private val onClickListener: OnClickListener) :
    ListAdapter<DisplayModel, TagSearchAdapter.TagPostViewHolder>(DiffUtilCallback) {

    /**
     * Live data to notify fragment that more posts should be retrieved from network
     */
    private val _postsExhausted = MutableLiveData<Boolean>()
    val postsExhausted: LiveData<Boolean>
        get() = _postsExhausted

    /**
     * Standard adapter methods
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagPostViewHolder {
        return TagPostViewHolder(ImagePreviewBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: TagPostViewHolder, position: Int) {
        val post = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(post, position)
        }
        holder.bind(post, position)
        if (position == itemCount - 5) {
            _postsExhausted.value = true
        }
    }

    /**
     * ViewHolder for posts. Width of imageView is set here, to scale image to edge of device screen
     */
    inner class TagPostViewHolder(val binding: ImagePreviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DisplayModel, itemNumber: Int) {
            binding.post = item
            binding.postNumber = itemNumber + 1
            binding.previewImageView.layoutParams.width = screenWidth / 2
            binding.executePendingBindings()
        }
    }

    /**
     * Diff util callback functions
     */
    companion object DiffUtilCallback : DiffUtil.ItemCallback<DisplayModel>() {
        override fun areItemsTheSame(oldItem: DisplayModel, newItem: DisplayModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DisplayModel, newItem: DisplayModel): Boolean {
            return oldItem == newItem
        }
    }

    /**
     * onClick listener for navigation
     */
    class OnClickListener(val clickListener: (post: DisplayModel, postNumber: Int) -> Unit) {
        fun onClick(post: DisplayModel, postNumber: Int) = clickListener(post, postNumber)
    }

    /**
     * Function to be called after more posts have been retrieved from network in order to reset detection system
     */
    fun doneGettingPosts() {
        _postsExhausted.value = false
    }
}