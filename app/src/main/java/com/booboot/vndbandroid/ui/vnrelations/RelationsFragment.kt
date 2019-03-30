package com.booboot.vndbandroid.ui.vnrelations

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.extensions.observe
import com.booboot.vndbandroid.extensions.observeOnce
import com.booboot.vndbandroid.extensions.openURL
import com.booboot.vndbandroid.extensions.openVN
import com.booboot.vndbandroid.extensions.startParentEnterTransition
import com.booboot.vndbandroid.model.vndb.Anime
import com.booboot.vndbandroid.model.vndb.Links
import com.booboot.vndbandroid.model.vndb.Relation
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.model.vndbandroid.EVENT_VNLIST_CHANGED
import com.booboot.vndbandroid.service.EventReceiver
import com.booboot.vndbandroid.ui.base.BaseFragment
import com.booboot.vndbandroid.ui.vndetails.VNDetailsFragment
import com.booboot.vndbandroid.util.GridAutofitLayoutManager
import com.booboot.vndbandroid.util.Pixels
import kotlinx.android.synthetic.main.relations_fragment.*
import kotlinx.android.synthetic.main.vn_card.view.*

class RelationsFragment : BaseFragment(), (Anime) -> Unit, (View, Relation, VN?) -> Unit {
    override val layout: Int = R.layout.relations_fragment
    private lateinit var viewModel: RelationsViewModel
    private lateinit var adapter: RelationsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        context?.let {
            recyclerView.layoutManager = GridAutofitLayoutManager(it, Pixels.px(300))
            adapter = RelationsAdapter(this, this)
            recyclerView.adapter = adapter

            val vnId = arguments?.getLong(VNDetailsFragment.EXTRA_VN_ID) ?: 0
            viewModel = ViewModelProviders.of(this).get(RelationsViewModel::class.java)
            viewModel.relationsData.observe(this, ::showRelations)
            viewModel.errorData.observeOnce(this, ::showError)
            viewModel.loadVn(vnId, false)

            EventReceiver(this).observe(mapOf(
                EVENT_VNLIST_CHANGED to { viewModel.loadVn(vnId, true) }
            ))
        }
    }

    private fun showRelations(relationsData: RelationsData?) {
        relationsData ?: return
        adapter.relationsData = relationsData
        startParentEnterTransition()
    }

    override fun invoke(anime: Anime) {
        context?.openURL(Links.ANIDB + anime.id)
    }

    override fun invoke(view: View, relation: Relation, vn: VN?) {
        vn ?: return
        vnDetailsFragment()?.viewModel?.let {
            findNavController().openVN(vn, view.image, it)
        }
    }
}