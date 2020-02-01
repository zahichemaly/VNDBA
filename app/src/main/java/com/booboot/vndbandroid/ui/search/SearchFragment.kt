package com.booboot.vndbandroid.ui.search

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.extensions.home
import com.booboot.vndbandroid.extensions.observeNonNull
import com.booboot.vndbandroid.extensions.observeOnce
import com.booboot.vndbandroid.extensions.onSubmitListener
import com.booboot.vndbandroid.extensions.setupStatusBar
import com.booboot.vndbandroid.extensions.setupToolbar
import com.booboot.vndbandroid.ui.base.BaseFragment
import com.booboot.vndbandroid.ui.vnlist.VNAdapter
import com.booboot.vndbandroid.ui.vnlist.VnlistData
import com.booboot.vndbandroid.util.GridAutofitLayoutManager
import com.booboot.vndbandroid.util.Pixels
import kotlinx.android.synthetic.main.floating_search_toolbar.*
import kotlinx.android.synthetic.main.search_fragment.*

class SearchFragment : BaseFragment<SearchViewModel>() {
    override val layout: Int = R.layout.search_fragment
    private val adapter by lazy { VNAdapter(::onVnClicked) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val activity = home() ?: return

        setupStatusBar(true, searchBarCardView)
        setupToolbar()
        activity.supportActionBar?.setDisplayShowTitleEnabled(false)

        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        viewModel.loadingData.observeNonNull(this, ::showLoading)
        viewModel.errorData.observeOnce(this, ::showError)
        viewModel.searchData.observeNonNull(this, ::showSearch)

        floatingSearchBar.setupWithContainer(constraintLayout, vnList)
        adapter.onUpdate = ::onAdapterUpdate
        vnList.setHasFixedSize(true)
        vnList.layoutManager = GridAutofitLayoutManager(activity, Pixels.px(300))
        vnList.adapter = adapter

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
            R.id.action_advanced_search -> {
            } // TODO
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showSearch(searchData: VnlistData) {
        vnList.isVisible = true
        adapter.data = searchData
    }

    override fun onAdapterUpdate(empty: Boolean) {
        super.onAdapterUpdate(empty)
        vnList.isVisible = !empty
    }
}