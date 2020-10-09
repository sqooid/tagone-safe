package com.example.tagone.detailedview

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.tagone.R
import com.example.tagone.databinding.DetailedViewFragmentBinding
import com.example.tagone.util.DisplayModel

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
         * Creating viewModel
         */
        viewModel = ViewModelProvider(this, DetailedViewViewModel.ViewModelFactory(post, postNumber)).get(
            DetailedViewViewModel::class.java
        )
        /**
         * Binding variables
         */
        binding.post = post
        binding.viewModel = viewModel

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.detailed_view_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.information_button -> this.findNavController().navigate(DetailedViewFragmentDirections.actionDetailedViewFragmentToDetailedInformation(post))
        }
        return super.onOptionsItemSelected(item)
    }
}