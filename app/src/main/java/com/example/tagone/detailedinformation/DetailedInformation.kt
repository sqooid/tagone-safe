package com.example.tagone.detailedinformation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tagone.R
import com.example.tagone.databinding.DetailedInformationFragmentBinding
import com.example.tagone.databinding.TagChipBinding
import com.example.tagone.util.DisplayModel
import com.google.android.flexbox.FlexboxLayout

class DetailedInformation : Fragment() {

    private lateinit var binding: DetailedInformationFragmentBinding
    private lateinit var viewModel: DetailedInformationViewModel
    private lateinit var coordinator: CoordinatorLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        /**
         * Getting args from navigation
         */
        val args = DetailedInformationArgs.fromBundle(requireArguments())
        val post = args.post
        /**
         * Creating data binding
         */
        binding = DetailedInformationFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.post = post
        /**
         * Binding viewModel
         */
        viewModel =
            ViewModelProvider(this, DetailedInformationViewModel.ViewModelFactory(post)).get(
                DetailedInformationViewModel::class.java
            )
        binding.viewModel = viewModel

        generateTags(post, inflater)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.linearLayout.removeAllViews()
    }

    private fun generateTags(post: DisplayModel, inflater: LayoutInflater) {
        addTagCards(post.tagStringCharacter.split(" "), binding.characterSegment, inflater)
        addTagCards(post.tagStringArtist.split(" "), binding.artistSegment, inflater)
        addTagCards(post.tagStringCopyright.split(" "), binding.copyrightSegment, inflater)
        addTagCards(post.tagStringGeneral.split(" "), binding.generalSegment, inflater)
        addTagCards(post.tagStringMeta.split(" "), binding.metaSegment, inflater)
    }

    private fun addTagCards(tags: List<String>, view: FlexboxLayout, inflater: LayoutInflater) {
        if (tags[0] == "") {
            val emptyView = inflater.inflate(R.layout.no_tags_placeholder, view, false)
            view.addView(emptyView)
        } else {
            for (tag in tags) {
                val binding = TagChipBinding.inflate(inflater)
                binding.tagChipTextView.text = tag
                view.addView(binding.root)
            }
        }
    }
}




