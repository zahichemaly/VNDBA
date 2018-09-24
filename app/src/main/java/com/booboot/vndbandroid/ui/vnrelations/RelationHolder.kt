package com.booboot.vndbandroid.ui.vnrelations

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.booboot.vndbandroid.extensions.toggle
import com.booboot.vndbandroid.model.vndb.RELATIONS
import com.booboot.vndbandroid.model.vndb.Relation
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.model.vndb.Vnlist
import com.booboot.vndbandroid.model.vndb.Votelist
import com.booboot.vndbandroid.model.vndb.Wishlist
import com.booboot.vndbandroid.model.vndbandroid.Preferences
import com.booboot.vndbandroid.model.vndbandroid.Priority
import com.booboot.vndbandroid.model.vndbandroid.Status
import com.booboot.vndbandroid.model.vndbandroid.Vote
import com.booboot.vndbandroid.util.image.DarkBlurTransform
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.vn_card.view.*

class RelationHolder(itemView: View, private val onClick: (View, Relation, VN?) -> Unit) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    private var relation: Relation? = null
    private var vn: VN? = null

    init {
        itemView.cardView.setOnClickListener(this)
    }

    fun onBind(relation: Relation, vn: VN?, vnlist: Vnlist?, votelist: Votelist?, wishlist: Wishlist?) = with(itemView) {
        this@RelationHolder.relation = relation
        this@RelationHolder.vn = vn

        title.text = relation.title
        subtitle.text = RELATIONS[relation.relation]
        vn?.let {
            val nsfw = it.image_nsfw && !Preferences.nsfw
            Picasso.get().load(it.image).transform(DarkBlurTransform(context, nsfw)).into(image)
            nsfwTag.toggle(nsfw)
        }

        statusButton.text = Status.toShortString(vnlist?.status)
        wishlistButton.text = Priority.toShortString(wishlist?.priority)
        votesButton.text = Vote.toShortString(votelist?.vote)
    }

    override fun onClick(view: View) {
        relation?.let { onClick(view, it, vn) }
    }
}