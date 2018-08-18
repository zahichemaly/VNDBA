package com.booboot.vndbandroid.ui.vnsummary

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.extensions.toggle
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.model.vndbandroid.LANGUAGES
import com.booboot.vndbandroid.model.vndbandroid.PLATFORMS
import com.booboot.vndbandroid.ui.base.BaseFragment
import com.booboot.vndbandroid.ui.vndetails.VNDetailsActivity
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.platform_tag.view.*
import kotlinx.android.synthetic.main.summary_fragment.*

class SummaryFragment : BaseFragment() {
    override val layout: Int = R.layout.summary_fragment
    private lateinit var viewModel: SummaryViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (activity == null) return

        val vnId = arguments?.getLong(VNDetailsActivity.EXTRA_VN_ID) ?: 0
        viewModel = ViewModelProviders.of(this).get(SummaryViewModel::class.java)
        viewModel.vnData.observe(this, Observer { showVn(it) })
        viewModel.errorData.observe(this, Observer { showError(it) })
        viewModel.loadVn(vnId, false)
    }

    private fun showVn(vn: VN?) {
        if (vn == null) return
        title.text = vn.title
        originalTitle.text = vn.original
        originalTitle.toggle(vn.original?.isNotEmpty() == true)
        aliases.text = vn.aliases?.split("\n")?.joinToString()
        aliases.toggle(vn.aliases?.isNotEmpty() == true)

        platforms.removeAllViews()
        vn.platforms.forEach {
            layoutInflater.inflate(R.layout.platform_tag, platforms, false).apply {
                val platform = PLATFORMS[it] ?: return@apply
                tagBackground.setCardBackgroundColor(ContextCompat.getColor(context, platform.color))
                tagText.text = platform.text
                platforms.addView(this)
            }
        }

        languages.removeAllViews()
        vn.languages.forEach {
            layoutInflater.inflate(R.layout.language_tag, languages, false).apply {
                val language = LANGUAGES[it] ?: return@apply
                val chip = this as Chip
                chip.text = language.text
                chip.chipIcon = VectorDrawableCompat.create(resources, language.flag, context.theme)
                languages.addView(this)
            }
        }
    }
}