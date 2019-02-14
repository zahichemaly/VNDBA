package com.booboot.vndbandroid.ui.vntags

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.extensions.observeOnce
import com.booboot.vndbandroid.model.vndbandroid.VNDetailsTags
import com.booboot.vndbandroid.model.vndbandroid.VNTag
import com.booboot.vndbandroid.ui.base.BaseFragment
import com.booboot.vndbandroid.ui.vndetails.VNDetailsFragment
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexboxItemDecoration
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import kotlinx.android.synthetic.main.tags_fragment.*

class TagsFragment : BaseFragment(), TagsAdapter.Callback {
    override val layout: Int = R.layout.tags_fragment
    private lateinit var viewModel: TagsViewModel
    private lateinit var tagsAdapter: TagsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val activity = activity ?: return

        val flexbox = FlexboxLayoutManager(context)
        flexbox.alignItems = AlignItems.FLEX_START
        flexbox.justifyContent = JustifyContent.CENTER
        val itemDecoration = FlexboxItemDecoration(activity)
        itemDecoration.setDrawable(ContextCompat.getDrawable(activity, R.drawable.flexbox_divider_8dp))
        itemDecoration.setOrientation(FlexboxItemDecoration.BOTH)
        recyclerView.addItemDecoration(itemDecoration)

        recyclerView.layoutManager = flexbox
        tagsAdapter = TagsAdapter(this)
        recyclerView.adapter = tagsAdapter

        val vnId = arguments?.getLong(VNDetailsFragment.EXTRA_VN_ID) ?: 0

        viewModel = ViewModelProviders.of(this).get(TagsViewModel::class.java)
        viewModel.errorData.observeOnce(this, ::showError)
        viewModel.tagsData.observe(this, Observer { showTags(it) })
        viewModel.tagsData.value?.let {
            /* [IMPORTANT] Filling the tags adapter immediately. When the fragment is recreated, if we let the ViewModel observer call this method,
            it is only called in onStart(), which is AFTER onViewRestoredState(), which is TOO LATE for the adapter to restore its state.
            So we have to manually call the callback now. */
            showTags(viewModel.tagsData.value)
        }

        viewModel.loadTags(vnId, false)
    }

    private fun showTags(tags: VNDetailsTags?) {
        if (tags == null) return
        tagsAdapter.items = tags
    }

    override fun onTitleClicked(title: String) {
        viewModel.collapseTags(title)
    }

    override fun onChipClicked(tag: VNTag) {
    }
}