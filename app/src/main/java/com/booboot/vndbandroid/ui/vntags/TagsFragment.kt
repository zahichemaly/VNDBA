package com.booboot.vndbandroid.ui.vntags

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.extensions.observe
import com.booboot.vndbandroid.extensions.observeOnce
import com.booboot.vndbandroid.extensions.restoreState
import com.booboot.vndbandroid.extensions.saveState
import com.booboot.vndbandroid.extensions.startParentEnterTransition
import com.booboot.vndbandroid.model.vndbandroid.VNDetailsTags
import com.booboot.vndbandroid.model.vndbandroid.VNTag
import com.booboot.vndbandroid.ui.base.BaseFragment
import com.booboot.vndbandroid.ui.vndetails.VNDetailsFragment
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexboxItemDecoration
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import kotlinx.android.synthetic.main.tags_fragment.*

class TagsFragment : BaseFragment<TagsViewModel>(), TagsAdapter.Callback {
    override val layout: Int = R.layout.tags_fragment
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
        viewModel.tagsData.observe(this, ::showTags)

        /* [IMPORTANT] Manually updating the UI now so the state can be restored */
        viewModel.tagsData.value?.let(::showTags)
        viewModel.loadTags(vnId, false)
    }

    private fun showTags(tags: VNDetailsTags) {
        tagsAdapter.items = tags
        recyclerView.restoreState(viewModel)
        startParentEnterTransition(tagsAdapter)
    }

    override fun onTitleClicked(title: String) {
        viewModel.collapseTags(title)
    }

    override fun onChipClicked(tag: VNTag) {
    }

    override fun onPause() {
        viewModel.layoutState = recyclerView.saveState()
        super.onPause()
    }
}