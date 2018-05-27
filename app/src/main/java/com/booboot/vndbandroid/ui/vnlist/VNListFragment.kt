package com.booboot.vndbandroid.ui.vnlist

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.factory.VNCardFactory
import com.booboot.vndbandroid.model.vndb.AccountItems
import com.booboot.vndbandroid.ui.base.BaseFragment
import com.booboot.vndbandroid.ui.home.MainActivity
import com.booboot.vndbandroid.ui.hometabs.HomeTabsFragment
import com.booboot.vndbandroid.ui.hometabs.HomeTabsFragment.Companion.VNLIST
import com.booboot.vndbandroid.util.Utils
import kotlinx.android.synthetic.main.vn_list_fragment.*

class VNListFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener {
    override val layout: Int = R.layout.vn_list_fragment
    private lateinit var vnListViewModel: VNListViewModel
    private lateinit var adapter: VNAdapter
    //    public static boolean refreshOnInit;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val tabValue = arguments?.getInt(HomeTabsFragment.TAB_VALUE_ARG) ?: -1
        val listType = arguments?.getInt(HomeTabsFragment.LIST_TYPE_ARG) ?: VNLIST

        vnListViewModel = ViewModelProviders.of(this).get(VNListViewModel::class.java)
        vnListViewModel.vnData.observe(this, Observer { showVns(it) })
        vnListViewModel.errorData.observe(this, Observer { showError(it) })
        vnListViewModel.getVns(listType, tabValue)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = VNCardFactory.setupList(activity, vnList, false, false, false, false, false)

        refreshLayout.setOnRefreshListener(this)
        refreshLayout.setColorSchemeColors(Utils.getThemeColor(activity, R.attr.colorAccent))
    }

    private fun showVns(accountItems: AccountItems?) {
        if (accountItems == null) return
        adapter.items = accountItems
        filter((activity as MainActivity).searchView?.query ?: "")
    }

    fun filter(search: CharSequence) = adapter.filter.filter(search)

    override fun onRefresh() {
        //            Cache.loadData(activity, object : Callback() {
        //                protected fun config() {
        //                    if (Cache.shouldRefreshView) {
        //                        if (activity is MainActivity && !activity.isDestroyed()) {
        //                            (activity as MainActivity).refreshVnlistFragment()
        //                        }
        //                    }
        //                    refreshLayout.setRefreshing(false)
        //                }
        //            }, object : Callback() {
        //                protected fun config() {
        //                    if (Cache.pipeliningError) return
        //                    Cache.pipeliningError = true
        //                    Callback.showToast(activity, message)
        //                    refreshLayout.setRefreshing(false)
        //                    if (Cache.countDownLatch != null) Cache.countDownLatch.countDown()
        //                }
        //            })
    }
}