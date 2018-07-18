package com.booboot.vndbandroid.ui.vnlist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.factory.VNCardFactory
import com.booboot.vndbandroid.model.vndb.AccountItems
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.ui.base.BaseFragment
import com.booboot.vndbandroid.ui.home.HomeActivity
import com.booboot.vndbandroid.ui.hometabs.HomeTabsFragment
import com.booboot.vndbandroid.ui.hometabs.HomeTabsFragment.Companion.VNLIST
import com.booboot.vndbandroid.ui.vndetails.VNDetailsActivity
import com.booboot.vndbandroid.util.Utils
import kotlinx.android.synthetic.main.vn_card.view.*
import kotlinx.android.synthetic.main.vn_list_fragment.*

class VNListFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener, (View, VN) -> Unit {
    override val layout: Int = R.layout.vn_list_fragment
    private lateinit var viewModel: VNListViewModel
    private lateinit var adapter: VNAdapter
    private var tabValue: Int = 0
    private var listType: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        tabValue = arguments?.getInt(HomeTabsFragment.TAB_VALUE_ARG) ?: -1
        listType = arguments?.getInt(HomeTabsFragment.LIST_TYPE_ARG) ?: VNLIST

        viewModel = ViewModelProviders.of(this).get(VNListViewModel::class.java)
        viewModel.vnData.observe(this, Observer { showVns(it) })
        viewModel.errorData.observe(this, Observer { showError(it) })
        update()

        return rootView
    }

    fun update() = viewModel.getVns(listType, tabValue)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = VNAdapter(this)
        VNCardFactory.setupList(activity, vnList, adapter)

        refreshLayout.setOnRefreshListener(this)
        refreshLayout.setColorSchemeColors(Utils.getThemeColor(activity, R.attr.colorAccent))
        showLoading(home()?.isLoading() == true)
    }

    private fun showVns(accountItems: AccountItems?) {
        if (accountItems == null) return
        adapter.items = accountItems
        filter((activity as HomeActivity).searchView?.query ?: "")
    }

    fun filter(search: CharSequence) = adapter.filter.filter(search)

    override fun invoke(itemView: View, vn: VN) {
        activity?.let {
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity!!, itemView.image, "slideshow")
            val intent = Intent(it, VNDetailsActivity::class.java)
            intent.putExtra(VNDetailsActivity.EXTRA_VN_ID, vn.id)
            intent.putExtra(VNDetailsActivity.EXTRA_SHARED_ELEMENT_COVER, vn.image)
            intent.putExtra(VNDetailsActivity.EXTRA_SHARED_ELEMENT_COVER_NSFW, vn.image_nsfw)
            it.startActivity(intent, options.toBundle())
        }
    }

    override fun onRefresh() {
        home()?.startupSync()
    }
}