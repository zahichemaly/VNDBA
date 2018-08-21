package com.booboot.vndbandroid.ui.vnsummary

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.extensions.formatText
import com.booboot.vndbandroid.extensions.openURL
import com.booboot.vndbandroid.extensions.toggle
import com.booboot.vndbandroid.model.vndb.Links
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.model.vndbandroid.LANGUAGES
import com.booboot.vndbandroid.model.vndbandroid.PLATFORMS
import com.booboot.vndbandroid.model.vndbandroid.Vote
import com.booboot.vndbandroid.ui.base.BaseFragment
import com.booboot.vndbandroid.ui.vndetails.VNDetailsActivity
import com.booboot.vndbandroid.util.Utils
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.info_bubble.view.*
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
        val context = context ?: return

        title.text = vn.title
        originalTitle.text = vn.original
        originalTitle.toggle(vn.original?.isNotEmpty() == true)
        aliases.text = vn.aliases?.split("\n")?.joinToString()
        aliases.toggle(vn.aliases?.isNotEmpty() == true)

        description.formatText(vn.description)
        description.toggle(description.text.isNotEmpty())

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

        val white = ContextCompat.getColor(context, R.color.white)
        length.apply {
            icon.setImageResource(R.drawable.ic_access_time_black_48dp)
            title.text = vn.lengthString()
            value.text = vn.lengthInHours()
            value.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        }
        released.apply {
            icon.setImageResource(R.drawable.ic_event_black_48dp)
            title.setText(R.string.released_on)
            value.text = Utils.formatDateMedium(context, vn.released)
            value.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        }
        with(popularity) {
            this as CardView
            setCardBackgroundColor(ContextCompat.getColor(context, vn.popularityColor()))
            icon.setImageResource(R.drawable.ic_whatshot_black_48dp)
            title.setText(R.string.popularity)
            value.text = String.format(getString(R.string.percent), vn.popularity)
            icon.imageTintList = ColorStateList.valueOf(white)
            title.setTextColor(white)
            value.setTextColor(white)
        }
        rating.apply {
            this as CardView
            setCardBackgroundColor(ContextCompat.getColor(context, Vote.getColor(vn.rating)))
            icon.setImageResource(R.drawable.ic_trending_up_black_48dp)
            title.text = String.format(getString(R.string.x_votes), vn.votecount)
            value.text = String.format("%.2f", vn.rating)
            icon.imageTintList = ColorStateList.valueOf(white)
            title.setTextColor(white)
            value.setTextColor(white)
        }

        wikipediaButton.toggle(vn.links.wikipedia?.isNotEmpty() == true)
        renaiButton.toggle(vn.links.renai?.isNotEmpty() == true)
        encubedButton.toggle(vn.links.encubed?.isNotEmpty() == true)

        wikipediaButton.setOnClickListener { context.openURL(Links.WIKIPEDIA + vn.links.wikipedia) }
        renaiButton.setOnClickListener { context.openURL(Links.RENAI + vn.links.renai) }
        encubedButton.setOnClickListener { context.openURL(Links.ENCUBED + vn.links.encubed) }
    }
}