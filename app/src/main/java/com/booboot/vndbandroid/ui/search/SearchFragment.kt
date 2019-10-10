package com.booboot.vndbandroid.ui.search

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.extensions.home
import com.booboot.vndbandroid.extensions.onStateChanged
import com.booboot.vndbandroid.extensions.removeFocus
import com.booboot.vndbandroid.extensions.setupStatusBar
import com.booboot.vndbandroid.extensions.setupToolbar
import com.booboot.vndbandroid.extensions.toggleBottomSheet
import com.booboot.vndbandroid.ui.base.BaseFragment
import com.booboot.vndbandroid.ui.base.HasSearchBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.search_bottom_sheet.*
import kotlinx.android.synthetic.main.search_fragment.*

class SearchFragment : BaseFragment<SearchViewModel>(), HasSearchBar {
    override val layout: Int = R.layout.search_fragment
    lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val activity = home() ?: return

        setupStatusBar(false)
        setupToolbar()
        activity.supportActionBar?.setDisplayShowTitleEnabled(false)

        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = viewModel.bottomSheetState
        bottomSheetBehavior.onStateChanged(
            onStateChanged = { viewModel.bottomSheetState = it },
            onCollapsed = {
                searchBar?.removeFocus()
                iconArrow.setImageResource(R.drawable.ic_keyboard_arrow_down_white_24dp)
            },
            onExpanding = {
                iconArrow.setImageResource(R.drawable.ic_keyboard_arrow_up_white_24dp)
            }
        )

        searchBarTextInputLayout.setEndIconOnClickListener {
            searchBar.text = null
        }
        bottomSheetHeader.setOnClickListener { bottomSheet.toggleBottomSheet() }
    }

    override fun searchBar(): View? = searchBar
}