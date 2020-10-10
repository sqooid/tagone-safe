package com.example.tagone.detailedview

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.tagone.R
import com.example.tagone.databinding.DetailedViewFragmentBinding
import com.example.tagone.databinding.TagChipBinding
import com.example.tagone.util.DisplayModel
import com.google.android.flexbox.FlexboxLayout

class DetailedViewFragment : Fragment() {

    private lateinit var viewModel: DetailedViewViewModel
    private lateinit var binding: DetailedViewFragmentBinding
    private lateinit var windowManager: WindowManager
    private lateinit var post: DisplayModel

    /**
     * Attempted shared element animation stuff
     */
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
//    }
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        val size = Point()
//        windowManager.defaultDisplay.getSize(size)
//        val height = size.y
//        binding.detailedImageView.layoutParams.height = height
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /**
         * Getting args from navigation
         */
        val args = DetailedViewFragmentArgs.fromBundle(requireArguments())
        post = args.post
        val postNumber = args.postNumber // The position of the post in list being navigated from

        /**
         * Creating data binding
         */
        binding = DetailedViewFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this

        /**
         * Getting windowManager and setting imageView constraints
         */
        windowManager = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val height = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            val display = windowManager.defaultDisplay
            val size = Point()
            display.getSize(size)
            size.y
        } else {
            windowManager.currentWindowMetrics.bounds.height()
        }
        binding.detailedImageView.layoutParams.height = height

        /**
         * Creating viewModel
         */
        val activity = requireNotNull(this.activity)
        viewModel = ViewModelProvider(
            this,
            DetailedViewViewModel.ViewModelFactory(activity.application, post, postNumber)
        ).get(
            DetailedViewViewModel::class.java
        )

        /**
         * Binding variables
         */
        binding.post = post
        binding.viewModel = viewModel

        /**
         * Generating xml for tags below post
         */
        generateTags(post, inflater)

        /**
         * Observers
         */
        // Observer for tags being clicked to start search using tag in tag search fragment
        viewModel.linkedTag.observe(viewLifecycleOwner, Observer {
            this.findNavController().navigate(DetailedViewFragmentDirections.followTag(it))
            viewModel.doneSearchingLinkedTag()
        })
        viewModel.isFavourited.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.favouriteButton.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.likeButtonBackground))
            } else {
                binding.favouriteButton.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.elevation2))
            }
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    private fun generateTags(post: DisplayModel, inflater: LayoutInflater) {
        addTagCards(post.tagStringCharacter.split(" "), binding.tags.characterSegment, inflater)
        addTagCards(post.tagStringArtist.split(" "), binding.tags.artistSegment, inflater)
        addTagCards(post.tagStringCopyright.split(" "), binding.tags.copyrightSegment, inflater)
        addTagCards(post.tagStringGeneral.split(" "), binding.tags.generalSegment, inflater)
        addTagCards(post.tagStringMeta.split(" "), binding.tags.metaSegment, inflater)
    }

    private fun addTagCards(tags: List<String>, view: FlexboxLayout, inflater: LayoutInflater) {
        if (tags[0] == "") {
            val emptyView = inflater.inflate(R.layout.no_tags_placeholder, view, false)
            view.addView(emptyView)
        } else {
            for (tag in tags) {
                val binding = TagChipBinding.inflate(inflater)
                binding.tagChipTextView.text = tag
                binding.tagChipContainer.setOnClickListener {
                    viewModel.searchLinkedTag(tag)
                }
                view.addView(binding.root)
            }
        }
    }
}