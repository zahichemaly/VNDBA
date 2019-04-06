package com.booboot.vndbandroid.ui.vnrelations

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.extensions.home
import com.booboot.vndbandroid.extensions.observe
import com.booboot.vndbandroid.extensions.observeOnce
import com.booboot.vndbandroid.extensions.openURL
import com.booboot.vndbandroid.extensions.openVN
import com.booboot.vndbandroid.extensions.restoreState
import com.booboot.vndbandroid.extensions.saveState
import com.booboot.vndbandroid.extensions.startParentEnterTransition
import com.booboot.vndbandroid.model.vndb.Anime
import com.booboot.vndbandroid.model.vndb.Links
import com.booboot.vndbandroid.model.vndb.Relation
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.ui.base.BaseFragment
import com.booboot.vndbandroid.ui.vndetails.VNDetailsFragment
import com.booboot.vndbandroid.util.GridAutofitLayoutManager
import com.booboot.vndbandroid.util.Pixels
import kotlinx.android.synthetic.main.relations_fragment.*
import kotlinx.android.synthetic.main.vn_card.view.*

class RelationsFragment : BaseFragment<RelationsViewModel>() {
    override val layout: Int = R.layout.relations_fragment
    private lateinit var adapter: RelationsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val context = context ?: return
        recyclerView.layoutManager = GridAutofitLayoutManager(context, Pixels.px(300))
        adapter = RelationsAdapter(::onAnimeClicked, ::onRelationClicked)
        recyclerView.adapter = adapter

        val vnId = arguments?.getLong(VNDetailsFragment.EXTRA_VN_ID) ?: 0
        viewModel = ViewModelProviders.of(this).get(RelationsViewModel::class.java)
        viewModel.relationsData.observeOnce(this, ::showRelations)
        viewModel.errorData.observeOnce(this, ::showError)
        home()?.viewModel?.accountData?.observe(this) { viewModel.loadVn(vnId) }
    }

    private fun showRelations(relationsData: RelationsData) {
        adapter.relationsData = relationsData
        startParentEnterTransition(adapter)
        recyclerView.restoreState(this)
    }

    private fun onAnimeClicked(anime: Anime) {
        context?.openURL(Links.ANIDB + anime.id)
    }

    private fun onRelationClicked(view: View, relation: Relation, vn: VN?) {
        vn ?: return
        vnDetailsFragment()?.viewModel?.let {
            findNavController().openVN(vn, view.image, it)
        }
    }

    override fun scrollToTop() {
        recyclerView.scrollToPosition(0)
    }

    override fun onPause() {
        layoutState = recyclerView.saveState()
        viewModel.layoutState = layoutState
        super.onPause()
    }
}