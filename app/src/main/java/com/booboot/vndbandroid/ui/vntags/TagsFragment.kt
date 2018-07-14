package com.booboot.vndbandroid.ui.vntags

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.model.vndbandroid.VNDetailsTags
import com.booboot.vndbandroid.model.vndbandroid.VNTag
import com.booboot.vndbandroid.ui.base.BaseFragment
import com.booboot.vndbandroid.ui.vndetails.VNDetailsActivity
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import kotlinx.android.synthetic.main.tags_fragment.*

class TagsFragment : BaseFragment(), TagsAdapter.Callback {
    override val layout: Int = R.layout.tags_fragment
    private lateinit var tagsViewModel: TagsViewModel
    private lateinit var tagsAdapter: TagsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (activity == null) return

        val flexbox = FlexboxLayoutManager(context)
        flexbox.alignItems = AlignItems.FLEX_START
        flexbox.justifyContent = JustifyContent.CENTER

        recyclerView.layoutManager = flexbox
        tagsAdapter = TagsAdapter(this)
        recyclerView.adapter = tagsAdapter

        val vnId = arguments?.getInt(VNDetailsActivity.EXTRA_VN_ID) ?: 0

        tagsViewModel = ViewModelProviders.of(this).get(TagsViewModel::class.java)
        tagsViewModel.errorData.observe(this, Observer { showError(it) })
        tagsViewModel.tagsData.observe(this, Observer { showTags(it) })
        tagsViewModel.tagsData.value?.let {
            /* [IMPORTANT] Filling the tags adapter immediately. When the fragment is recreated, if we let the ViewModel observer call this method,
            it is only called in onStart(), which is AFTER onViewRestoredState(), which is TOO LATE for the adapter to restore its state.
            So we have to manually call the callback now. */
            showTags(tagsViewModel.tagsData.value)
        }

        tagsViewModel.loadTags(vnId, false)
    }

    private fun showTags(tags: VNDetailsTags?) {
        if (tags == null) return
        tagsAdapter.items = tags
    }

    override fun onTitleClicked(title: String) {
    }

    override fun onChipClicked(tag: VNTag) {
    }
}