package com.booboot.vndbandroid.ui.vntags

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.extensions.fastScroll
import com.booboot.vndbandroid.extensions.observeNonNull
import com.booboot.vndbandroid.extensions.observeOnce
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

class TagsFragment : BaseFragment<TagsViewModel>() {
    override val layout = R.layout.tags_fragment
    private val tagsAdapter = TagsAdapter(::onTitleClicked, ::onTagClicked)

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
        tagsAdapter.onUpdate = ::onAdapterUpdate
        recyclerView.adapter = tagsAdapter
        recyclerView.fastScroll()

        val vnId = arguments?.getLong(VNDetailsFragment.EXTRA_VN_ID) ?: 0

        viewModel = ViewModelProvider(this).get(TagsViewModel::class.java)
        viewModel.errorData.observeOnce(this, ::showError)
        viewModel.tagsData.observeNonNull(this, ::showTags)

        /* [IMPORTANT] Manually updating the UI now so the state can be restored */
        viewModel.tagsData.value?.let(::showTags)
        viewModel.loadTags(vnId, false)
    }

    private fun showTags(tags: VNDetailsTags) {
        tagsAdapter.items = tags
        startParentEnterTransition()
    }

    private fun onTitleClicked(title: String) {
        viewModel.collapseTags(title)
    }

    private fun onTagClicked(tag: VNTag) {
    }

    override fun scrollToTop() {
        recyclerView.scrollToPosition(0)
    }
}