package com.example.tagone.tagsearch

import android.app.SearchManager
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.loader.app.LoaderManager
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.tagone.R
import com.example.tagone.databinding.TagSearchFragmentBinding
import com.example.tagone.util.Constants
import com.example.tagone.util.PostScrollAdapter

class TagSearchFragment : Fragment() {

    companion object {
        fun newInstance() = TagSearchFragment()
    }

    private lateinit var viewModel: TagSearchViewModel
    private lateinit var binding: TagSearchFragmentBinding
    private lateinit var windowManager: WindowManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Data Binding
        binding = TagSearchFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this

        // Defining window manager to get width of screen to be passed to recycler view layout manager
        windowManager = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        // ViewModel bound
        val activity = requireNotNull(this.activity)
        viewModel = ViewModelProvider(this, TagSearchViewModel.viewModelFactory(activity.application)).get(TagSearchViewModel::class.java)
        binding.viewModel = viewModel

        // Search field
        setHasOptionsMenu(true)

        // Calling function to set up recycler view
        bindRecyclerView()

        /**
         * Observers
         */
        viewModel.postSuccess.observe(viewLifecycleOwner, Observer {
            if (!it) {
                Toast.makeText(activity, "No posts with those tags", Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.searchParameters.observe(viewLifecycleOwner, Observer {
            updateToolbarTitle(it)
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = TagSearchFragmentArgs.fromBundle(requireArguments())
        if (args.linkedTag != "") {
            viewModel.doInitialSearchWithTags(args.linkedTag)
        }
    }


    /**
     * Helper functions
     */
    private fun updateToolbarTitle(text: String) {
        val toolBar = activity?.findViewById<Toolbar>(R.id.toolbar)
        toolBar?.title = text
    }


    /**
     * Options menu (containing searchView) creation and configuration
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.tag_search_options_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

        /**
         * Search field in toolbar
         */
        val searchItem = menu.findItem(R.id.tag_search_field)
        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = searchItem?.actionView as SearchView

        with(searchView) {
            setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
            /**
             * Setting text change and submit listener
             */
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    searchItem.collapseActionView()
                    if (query != null) {
                        viewModel.doInitialSearchWithTags(query)
                        Log.i("Test", "Search made")
                    }
                    val inputMethodManager =
                        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })

            /**
             * Setting focus change listener
             */
            setOnQueryTextFocusChangeListener { view, hasFocus ->
                val inputMethodManager =
                    context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                if (hasFocus) {
                    inputMethodManager.showSoftInput(view, 0)
                } else {
                    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                }
            }
        }
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
        val columns = preferences.getInt("scroll_columns_tag_search", 2)
        val lowResMode = preferences.getBoolean("use_preview_tag_search", false)

        /**
         * Instantiating adapter
         */
        val adapter = PostScrollAdapter(columns, width, PostScrollAdapter.OnClickListener { post, postNumber ->
            this.findNavController()
                .navigate(TagSearchFragmentDirections.tagSearchShowDetailed(post, postNumber))
        }, lowResMode)
        binding.tagSearchRecyclerView.adapter = adapter

        /**
         * Creating staggered grid layout manager
         */
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(columns, LinearLayoutManager.VERTICAL)
        staggeredGridLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        binding.tagSearchRecyclerView.layoutManager = staggeredGridLayoutManager

        /**
         * Detecting when scrolling has neared end of retrieved posts
         */
        adapter.postsExhausted.observe(viewLifecycleOwner, Observer {
            if (it) {
                viewModel.getMorePosts()
                adapter.doneGettingPosts()
            }
        })
    }
}
