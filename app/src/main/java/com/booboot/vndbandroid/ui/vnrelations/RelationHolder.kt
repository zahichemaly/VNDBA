package com.booboot.vndbandroid.ui.vnrelations

import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.booboot.vndbandroid.extensions.showNsfwImage
import com.booboot.vndbandroid.model.vndb.RELATIONS
import com.booboot.vndbandroid.model.vndb.Relation
import com.booboot.vndbandroid.model.vndb.UserList
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.model.vndbandroid.Vote
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.nsfw_tag.view.*
import kotlinx.android.synthetic.main.vn_card.view.*

class RelationHolder(override val containerView: View, private val onClick: (View, VN?) -> Unit) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    fun onBind(relation: Relation, vn: VN?, userList: UserList?) = with(itemView) {
        cardView.setOnClickListener { onClick(containerView, vn) }

        title.text = relation.title
        subtitle.text = RELATIONS[relation.relation]
        vn?.let {
            image.transitionName = "slideshow" + vn.id
            image.showNsfwImage(it.image, it.image_nsfw, nsfwTag)
        }

        votesButton.isVisible = userList?.vote != null
        votesButton.text = Vote.toShortString(userList?.vote)
        votesButton.background = ContextCompat.getDrawable(containerView.context, Vote.getDrawableColor10(userList?.vote))
    }
}