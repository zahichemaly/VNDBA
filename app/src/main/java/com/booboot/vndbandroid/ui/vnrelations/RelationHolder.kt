package com.booboot.vndbandroid.ui.vnrelations

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.booboot.vndbandroid.extensions.showNsfwImage
import com.booboot.vndbandroid.model.vndb.Label
import com.booboot.vndbandroid.model.vndb.RELATIONS
import com.booboot.vndbandroid.model.vndb.Relation
import com.booboot.vndbandroid.model.vndb.UserList
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.model.vndbandroid.Vote
import kotlinx.android.synthetic.main.nsfw_tag.view.*
import kotlinx.android.synthetic.main.vn_card.view.*

class RelationHolder(itemView: View, private val onClick: (View, VN?) -> Unit) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    private var relation: Relation? = null
    private var vn: VN? = null

    init {
        itemView.cardView.setOnClickListener(this)
    }

    fun onBind(relation: Relation, vn: VN?, userList: UserList?) = with(itemView) {
        this@RelationHolder.relation = relation
        this@RelationHolder.vn = vn

        title.text = relation.title
        subtitle.text = RELATIONS[relation.relation]
        vn?.let {
            image.transitionName = "slideshow" + vn.id
            image.showNsfwImage(it.image, it.image_nsfw, nsfwTag)
        }

        statusButton.text = Label.toShortString(userList?.firstStatus()?.id)
        wishlistButton.text = Label.toShortString(userList?.firstWishlist()?.id)
        votesButton.text = Vote.toShortString(userList?.vote)
    }

    override fun onClick(view: View) {
        relation?.let { onClick(view, vn) }
    }
}