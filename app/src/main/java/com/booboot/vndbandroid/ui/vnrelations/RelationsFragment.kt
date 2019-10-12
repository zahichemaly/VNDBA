package com.booboot.vndbandroid.ui.vnrelations

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.extensions.home
import com.booboot.vndbandroid.extensions.observeNonNull
import com.booboot.vndbandroid.extensions.observeOnce
import com.booboot.vndbandroid.extensions.openURL
import com.booboot.vndbandroid.extensions.openVN
import com.booboot.vndbandroid.extensions.startParentEnterTransition
import com.booboot.vndbandroid.model.vndb.Anime
import com.booboot.vndbandroid.model.vndb.Links
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.ui.base.BaseFragment
import com.booboot.vndbandroid.ui.vndetails.VNDetailsFragment
import com.booboot.vndbandroid.util.GridAutofitLayoutManager
import com.booboot.vndbandroid.util.Pixels
import kotlinx.android.synthetic.main.relations_fragment.*
import kotlinx.android.synthetic.main.vn_card.view.*

class RelationsFragment : BaseFragment<RelationsViewModel>() {
    override val layout: Int = R.layout.relations_fragment
    private val adapter: RelationsAdapter by lazy { RelationsAdapter(::onAnimeClicked, ::onRelationClicked) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val context = context ?: return
        recyclerView.layoutManager = GridAutofitLayoutManager(context, Pixels.px(300))
        adapter.onUpdate = ::onAdapterUpdate
        recyclerView.adapter = adapter

        val vnId = arguments?.getLong(VNDetailsFragment.EXTRA_VN_ID) ?: 0
        viewModel = ViewModelProviders.of(this).get(RelationsViewModel::class.java)
        viewModel.relationsData.observeOnce(this, ::showRelations)
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

    private fun onRelationClicked(view: View, vn: VN?) {
        vn ?: return
        vnDetailsFragment()?.viewModel?.let {
            findNavController().openVN(vn, view.image, it)
        }
    }

    override fun scrollToTop() {
        recyclerView.scrollToPosition(0)
    }
}