package com.booboot.vndbandroid.ui.vnlist

import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.extensions.showNsfwImage
import com.booboot.vndbandroid.model.vndb.Label
import com.booboot.vndbandroid.model.vndb.UserList
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.model.vndbandroid.Vote
import com.booboot.vndbandroid.util.Utils
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.label_tag.*
import kotlinx.android.synthetic.main.nsfw_tag.*
import kotlinx.android.synthetic.main.vn_card.*

class VNHolder(override val containerView: View, private val onVnClicked: (View, VN) -> Unit) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    val adapter = GroupAdapter<GroupieViewHolder>()

    init {
        labels.adapter = adapter
        labels.layoutManager = FlexboxLayoutManager(containerView.context).apply {
            alignItems = AlignItems.CENTER
            justifyContent = JustifyContent.CENTER
        }
        labels.suppressLayout(true)
    }

    fun bind(
        vn: VN,
        userList: UserList?,
        showFullDate: Boolean,
        showRank: Boolean,
        showRating: Boolean,
        showPopularity: Boolean,
        showVoteCount: Boolean
    ) {
        cardView.setOnClickListener { onVnClicked(containerView, vn) }

        val titleText = StringBuilder()
        val subtitleText = StringBuilder()
        if (showRank)
            titleText.append("#").append(adapterPosition + 1).append(containerView.context.getString(R.string.dash))
        titleText.append(vn.title)
        when {
            showRating -> subtitleText.append(vn.rating).append(" (").append(Vote.getName(vn.rating)).append(")")
            showPopularity -> subtitleText.append(vn.popularity).append("%")
            showVoteCount -> subtitleText.append(vn.votecount).append(" votes")
            else -> subtitleText.append(Utils.getDate(vn.released, showFullDate))
        }

        subtitleText.append(containerView.context.getString(R.string.bullet))
        if (vn.length ?: 0 > 0)
            subtitleText.append(vn.lengthFull())
        else
            subtitleText.append(Utils.getDate(vn.released, true))

        image.transitionName = "slideshow" + vn.id
        image.showNsfwImage(vn.image, vn.image_nsfw, nsfwTag)

        title.text = titleText
        subtitle.text = subtitleText
        votesButton.isVisible = userList?.vote != null
        votesButton.text = Vote.toShortString(userList?.vote)
        votesButton.background = ContextCompat.getDrawable(containerView.context, Vote.getDrawableColor10(userList?.vote))

        adapter.update(userList?.labelItems ?: listOf())
    }
}

data class VNLabelItem(var label: Label) : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.apply {
            tagText.text = label.label
            tagText.setTextColor(label.textColor(containerView.context))
            tagText.setBackgroundColor(label.backgroundColor(containerView.context))
        }
    }

    override fun getId() = label.id
    override fun getLayout() = R.layout.label_tag
}