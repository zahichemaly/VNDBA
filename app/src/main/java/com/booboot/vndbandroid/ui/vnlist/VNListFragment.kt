package com.booboot.vndbandroid.ui.vnlist

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.extensions.getThemeColor
import com.booboot.vndbandroid.extensions.hideOnBottom
import com.booboot.vndbandroid.extensions.home
import com.booboot.vndbandroid.extensions.observeNonNull
import com.booboot.vndbandroid.extensions.observeOnce
import com.booboot.vndbandroid.extensions.openVN
import com.booboot.vndbandroid.extensions.startParentEnterTransition
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.model.vndbandroid.Status
import com.booboot.vndbandroid.model.vndbandroid.VnlistData
import com.booboot.vndbandroid.ui.base.BaseFragment
import com.booboot.vndbandroid.ui.hometabs.HomeTabsFragment
import com.booboot.vndbandroid.ui.hometabs.HomeTabsFragment.Companion.VNLIST
import com.booboot.vndbandroid.util.GridAutofitLayoutManager
import com.booboot.vndbandroid.util.Pixels
import kotlinx.android.synthetic.main.home_tabs_fragment.*
import kotlinx.android.synthetic.main.vn_card.view.*
import kotlinx.android.synthetic.main.vn_list_fragment.*

class VNListFragment : BaseFragment<VNListViewModel>(), SwipeRefreshLayout.OnRefreshListener {
    override val layout: Int = R.layout.vn_list_fragment
    private val adapter by lazy { VNAdapter(::onVnClicked, filteredVns = viewModel.filteredVns) }
    private var tabValue: Int = 0
    private var listType: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val context = context ?: return

        tabValue = arguments?.getInt(HomeTabsFragment.TAB_VALUE_ARG) ?: Status.PLAYING
        listType = arguments?.getInt(HomeTabsFragment.LIST_TYPE_ARG) ?: VNLIST

        viewModel = ViewModelProviders.of(this).get(VNListViewModel::class.java)
        viewModel.errorData.observeOnce(this, ::showError)
        home()?.viewModel?.filterData?.observeNonNull(this, ::filter)
        home()?.viewModel?.loadingData?.observeNonNull(this, ::showLoading)
        homeTabs()?.viewModel?.vnlistData?.observeNonNull(this) { showVns(it) }

        adapter.onUpdate = ::onAdapterUpdate
        vnList.setHasFixedSize(true)
        vnList.layoutManager = GridAutofitLayoutManager(context, Pixels.px(300))
        vnList.adapter = adapter
        vnList.hideOnBottom(homeTabs()?.floatingSearchButton)

        backgroundInfo.setButtonOnClickListener { findNavController().navigate(R.id.searchFragment) }
        refreshLayout.setOnRefreshListener(this)
        refreshLayout.setColorSchemeColors(context.getThemeColor(R.attr.colorAccent))
    }

    private fun showVns(vnlistData: VnlistData) {
        val accountItems = vnlistData.items[tabValue] ?: return
        adapter.filterString = home()?.viewModel?.filterData?.value ?: ""
        adapter.items = accountItems

        startParentEnterTransition()
    }

    override fun onAdapterUpdate(empty: Boolean) {
        super.onAdapterUpdate(empty)
        viewModel.filteredVns = adapter.filteredVns
    }

    private fun filter(search: CharSequence) {
        adapter.filter.filter(search)
    }

    private fun onVnClicked(itemView: View, vn: VN) {
        homeTabs()?.viewModel?.let {
            findNavController().openVN(vn, itemView.image, it)
        }
    }

    override fun onRefresh() {
        home()?.startupSync()
    }

    override fun scrollToTop() {
        vnList.scrollToPosition(0)
    }

    private fun homeTabs() = parentFragment as? HomeTabsFragment?
}