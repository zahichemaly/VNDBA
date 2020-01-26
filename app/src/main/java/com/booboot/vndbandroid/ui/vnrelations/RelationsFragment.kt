package com.booboot.vndbandroid.ui.vnrelations

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.extensions.home
import com.booboot.vndbandroid.extensions.observeNonNull
import com.booboot.vndbandroid.extensions.observeOnce
import com.booboot.vndbandroid.extensions.openURL
import com.booboot.vndbandroid.extensions.startParentEnterTransition
import com.booboot.vndbandroid.model.vndb.Anime
import com.booboot.vndbandroid.model.vndb.Links
import com.booboot.vndbandroid.ui.base.BaseFragment
import com.booboot.vndbandroid.ui.vndetails.VNDetailsFragment
import com.booboot.vndbandroid.util.GridAutofitLayoutManager
import com.booboot.vndbandroid.util.Pixels
import kotlinx.android.synthetic.main.relations_fragment.*

class RelationsFragment : BaseFragment<RelationsViewModel>() {
    override val layout = R.layout.relations_fragment
    private val adapter by lazy { RelationsAdapter(::onAnimeClicked, ::onVnClicked) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val context = context ?: return
        recyclerView.layoutManager = GridAutofitLayoutManager(context, Pixels.px(300))
        adapter.onUpdate = ::onAdapterUpdate
        recyclerView.adapter = adapter

        val vnId = arguments?.getLong(VNDetailsFragment.EXTRA_VN_ID) ?: 0
        viewModel = ViewModelProvider(this).get(RelationsViewModel::class.java)
        viewModel.relationsData.observeNonNull(this, ::showRelations)
        viewModel.errorData.observeOnce(this, ::showError)
        home()?.viewModel?.accountData?.observeNonNull(this) { viewModel.loadVn(vnId, adapter.relationsData) }
    }

    private fun showRelations(relationsData: RelationsData) {
        adapter.relationsData = relationsData
        startParentEnterTransition()
    }

    private fun onAnimeClicked(anime: Anime) {
        context?.openURL(Links.ANIDB + anime.id)
    }

    override fun scrollToTop() {
        recyclerView.scrollToPosition(0)
    }
}