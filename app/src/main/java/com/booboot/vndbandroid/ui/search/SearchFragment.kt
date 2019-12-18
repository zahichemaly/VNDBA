package com.booboot.vndbandroid.ui.search

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.extensions.addPaddingTop
import com.booboot.vndbandroid.extensions.home
import com.booboot.vndbandroid.extensions.observeNonNull
import com.booboot.vndbandroid.extensions.observeOnce
import com.booboot.vndbandroid.extensions.onSubmitListener
import com.booboot.vndbandroid.extensions.setTextChangedListener
import com.booboot.vndbandroid.extensions.setupStatusBar
import com.booboot.vndbandroid.extensions.setupToolbar
import com.booboot.vndbandroid.extensions.statusBarHeight
import com.booboot.vndbandroid.extensions.toggle
import com.booboot.vndbandroid.model.vndb.AccountItems
import com.booboot.vndbandroid.ui.base.BaseFragment
import com.booboot.vndbandroid.ui.vnlist.VNAdapter
import com.booboot.vndbandroid.util.GridAutofitLayoutManager
import com.booboot.vndbandroid.util.Pixels
import kotlinx.android.synthetic.main.search_fragment.*

class SearchFragment : BaseFragment<SearchViewModel>() {
    override val layout: Int = R.layout.search_fragment
    private val adapter by lazy { VNAdapter(::onVnClicked, filteredVns = viewModel.filteredVns) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val activity = home() ?: return

        setupStatusBar(true, searchBarCardView)
        setupToolbar()
        activity.supportActionBar?.setDisplayShowTitleEnabled(false)
        searchBarParent.layoutTransition.setDuration(200)

        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
        viewModel.loadingData.observeNonNull(this, ::showLoading)
        viewModel.errorData.observeOnce(this, ::showError)
        viewModel.searchData.observeNonNull(this, ::showSearch)

        vnList.addPaddingTop(statusBarHeight())
        adapter.onUpdate = ::onAdapterUpdate
        vnList.setHasFixedSize(true)
        vnList.layoutManager = GridAutofitLayoutManager(activity, Pixels.px(300))
        vnList.adapter = adapter

        searchBarTextInputLayout.isEndIconVisible = false
        searchBar.setTextChangedListener {
            searchBarTextInputLayout.isEndIconVisible = it.isNotEmpty()
        }
        searchBarTextInputLayout.setEndIconOnClickListener {
            searchBar.text = null
        }
        searchBar.onSubmitListener {
            searchBar?.clearFocus()
            viewModel.search(searchBar?.text?.toString() ?: "")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_fragment, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_advanced_search -> advancedSearchLayout.toggle()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showSearch(items: AccountItems) {
        adapter.items = items
    }

    override fun onAdapterUpdate(empty: Boolean) {
        super.onAdapterUpdate(empty)
        viewModel.filteredVns = adapter.filteredVns
    }
}