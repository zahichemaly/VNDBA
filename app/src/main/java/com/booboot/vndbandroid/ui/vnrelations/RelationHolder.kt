package com.booboot.vndbandroid.ui.vnrelations

import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.extensions.showNsfwImage
import com.booboot.vndbandroid.model.vndb.RELATIONS
import com.booboot.vndbandroid.model.vndb.Relation
import com.booboot.vndbandroid.model.vndb.UserList
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.model.vndbandroid.Vote
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.nsfw_tag.*
import kotlinx.android.synthetic.main.vn_card.*

class RelationHolder(override val containerView: View, private val onClick: (View, VN?) -> Unit) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    val adapter = GroupAdapter<GroupieViewHolder>()

    init {
        labels.adapter = adapter
        labels.layoutManager = FlexboxLayoutManager(containerView.context).apply {
            alignItems = AlignItems.CENTER
            justifyContent = JustifyContent.CENTER
        }
        labels.suppressLayout(true)
    }

    fun onBind(relation: Relation, vn: VN?, userList: UserList?) = with(itemView) {
        cardView.setOnClickListener { onClick(containerView, vn) }

        title.text = relation.title
        subtitle.text = RELATIONS[relation.relation]
        vn?.let {
            image.transitionName = "slideshow" + vn.id
            image.showNsfwImage(it.image, it.image_nsfw, nsfwTag)
        }

        voteButton.isVisible = userList?.vote != null
        voteButton.text = Vote.toShortString(userList?.vote)
        voteButton.background = ContextCompat.getDrawable(containerView.context, Vote.getDrawableColor10(userList?.vote))

        val showLabels = userList != null && userList.labelItems.isNotEmpty()
        labels.isVisible = showLabels
        noLabelText.isVisible = !showLabels

        when {
            userList == null -> noLabelText.setText(R.string.not_in_your_list)
            userList.labelItems.isEmpty() -> noLabelText.setText(R.string.no_labels)
            else -> adapter.update(userList.labelItems)
        }
    }
}