package com.example.tagone.favourites

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.tagone.R
import com.example.tagone.databinding.FavouritesFragmentBinding
import com.example.tagone.tagsearch.TagSearchFragmentDirections
import com.example.tagone.util.Constants
import com.example.tagone.util.PostScrollAdapter

class FavouritesFragment : Fragment() {

    private lateinit var viewModel: FavouritesViewModel
    private lateinit var binding: FavouritesFragmentBinding
    private lateinit var windowManager: WindowManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FavouritesFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this

        /**
         * ViewModel
         */
        val activity = requireNotNull(this.activity)
        viewModel =
            ViewModelProvider(this, FavouritesViewModel.ViewModelFactory(activity.application)).get(
                FavouritesViewModel::class.java
            )
        binding.viewModel = viewModel

        windowManager = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        bindRecyclerView()

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favourites_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.download_all -> {
                if (!viewModel.downloadInProgress) {
                    viewModel.initiateDownloadAll()
                    viewModel.showNotification(this.requireContext())
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Function to set up recycler view with staggered grid layout
     */
    private fun bindRecyclerView() {
        /**
         * Getting width of device in order to scale image to edges
         */
        val width: Int
        width = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            val display = windowManager.defaultDisplay
            val size = Point()
            display.getSize(size)
            size.x
        } else {
            windowManager.currentWindowMetrics.bounds.width()
        }
        val preferences = requireContext().getSharedPreferences(Constants.PREFERENCE_NAME, 0)
        val columns = preferences.getInt("scroll_columns_favourites", 2)
        val lowResMode = preferences.getBoolean("use_preview_favourites", false)

        /**
         * Instantiating adapter
         */
        val adapter = PostScrollAdapter(
            columns,
            width,
            PostScrollAdapter.OnClickListener { post, postNumber ->
                this.findNavController()
                    .navigate(FavouritesFragmentDirections.favouritesShowDetailed(post, postNumber))
            }, lowResMode)
        binding.favouritesRecyclerView.adapter = adapter

        /**
         * Creating staggered grid layout manager
         */
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        staggeredGridLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        binding.favouritesRecyclerView.layoutManager = staggeredGridLayoutManager
    }

}
