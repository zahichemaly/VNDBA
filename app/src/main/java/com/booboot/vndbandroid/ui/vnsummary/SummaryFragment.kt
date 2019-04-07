package com.booboot.vndbandroid.ui.vnsummary

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.TypedValue
import android.view.View
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.extensions.adjustAlpha
import com.booboot.vndbandroid.extensions.darken
import com.booboot.vndbandroid.extensions.dayNightTheme
import com.booboot.vndbandroid.extensions.observe
import com.booboot.vndbandroid.extensions.observeOnce
import com.booboot.vndbandroid.extensions.openURL
import com.booboot.vndbandroid.extensions.scrollToTop
import com.booboot.vndbandroid.extensions.startParentEnterTransition
import com.booboot.vndbandroid.extensions.toggle
import com.booboot.vndbandroid.model.vndb.Links
import com.booboot.vndbandroid.model.vndbandroid.LANGUAGES
import com.booboot.vndbandroid.model.vndbandroid.PLATFORMS
import com.booboot.vndbandroid.model.vndbandroid.Vote
import com.booboot.vndbandroid.ui.base.BaseFragment
import com.booboot.vndbandroid.ui.vndetails.VNDetailsFragment
import com.booboot.vndbandroid.util.Utils
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.info_bubble.view.*
import kotlinx.android.synthetic.main.platform_tag.view.*
import kotlinx.android.synthetic.main.summary_fragment.*

class SummaryFragment : BaseFragment<SummaryViewModel>() {
    override val layout: Int = R.layout.summary_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (activity == null) return

        val vnId = arguments?.getLong(VNDetailsFragment.EXTRA_VN_ID) ?: 0
        viewModel = ViewModelProviders.of(this).get(SummaryViewModel::class.java)
        viewModel.summaryData.observe(this, ::showVn)
        viewModel.errorData.observeOnce(this, ::showError)
        viewModel.loadVn(vnId, false)
    }

    private fun showVn(summaryVN: SummaryVN) {
        context?.let { ctx ->
            val vn = summaryVN.vn

            title.text = vn.title
            originalTitle.text = vn.original
            originalTitle.toggle(vn.original?.isNotEmpty() == true)
            aliases.text = vn.aliases
            aliases.toggle(vn.aliases?.isNotEmpty() == true)

            description.movementMethod = LinkMovementMethod.getInstance()
            description.text = summaryVN.description
            description.toggle(description.text.isNotEmpty())

            val asyncInflater = AsyncLayoutInflater(ctx)
            platforms.removeAllViews()
            vn.platforms.forEach {
                asyncInflater.inflate(R.layout.platform_tag, platforms) { view, _, _ ->
                    context?.let { ctx ->
                        val platform = PLATFORMS[it] ?: return@inflate
                        view.tagText.text = platform.text
                        val color = ContextCompat.getColor(ctx, platform.color)
                        view.tagText.setTextColor(if (ctx.dayNightTheme() == "light") color.darken() else color)
                        view.tagText.setBackgroundColor(color.adjustAlpha(0.157f))
                        platforms?.addView(view)
                    }
                }
            }

            languages.removeAllViews()
            vn.languages.forEach {
                asyncInflater.inflate(R.layout.language_tag, languages) { view, _, _ ->
                    context?.let { ctx ->
                        val language = LANGUAGES[it] ?: return@inflate
                        val chip = view as Chip
                        chip.text = language.text
                        chip.chipIcon = VectorDrawableCompat.create(ctx.resources, language.flag, ctx.theme)
                        languages?.addView(chip)
                    }
                }
            }

            val white = ContextCompat.getColor(ctx, R.color.white)
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

            wikipediaButton.setOnClickListener { ctx.openURL(Links.WIKIPEDIA + vn.links.wikipedia) }
            renaiButton.setOnClickListener { ctx.openURL(Links.RENAI + vn.links.renai) }
            encubedButton.setOnClickListener { ctx.openURL(Links.ENCUBED + vn.links.encubed) }
            startParentEnterTransition()
        }
    }

    override fun scrollToTop() {
        scrollView.scrollToTop()
    }
}