package com.booboot.vndbandroid.ui.search

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.extensions.home
import com.booboot.vndbandroid.extensions.setTextChangedListener
import com.booboot.vndbandroid.extensions.setupStatusBar
import com.booboot.vndbandroid.extensions.setupToolbar
import com.booboot.vndbandroid.extensions.toggle
import com.booboot.vndbandroid.ui.base.BaseFragment
import kotlinx.android.synthetic.main.search_fragment.*

class SearchFragment : BaseFragment<SearchViewModel>() {
    override val layout: Int = R.layout.search_fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val activity = home() ?: return

        setupStatusBar(false)
        setupToolbar()
        activity.supportActionBar?.setDisplayShowTitleEnabled(false)
        searchBarParent.layoutTransition.setDuration(200)

        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)

        searchBarTextInputLayout.isEndIconVisible = false
        searchBar.setTextChangedListener {
            searchBarTextInputLayout.isEndIconVisible = it.isNotEmpty()
        }
        searchBarTextInputLayout.setEndIconOnClickListener {
            searchBar.text = null
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
}