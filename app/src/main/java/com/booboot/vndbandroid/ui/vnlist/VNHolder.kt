package com.booboot.vndbandroid.ui.vnlist

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.model.vndb.Vnlist
import com.booboot.vndbandroid.model.vndb.Votelist
import com.booboot.vndbandroid.model.vndb.Wishlist
import com.booboot.vndbandroid.model.vndbandroid.Preferences
import com.booboot.vndbandroid.model.vndbandroid.Priority
import com.booboot.vndbandroid.model.vndbandroid.Status
import com.booboot.vndbandroid.model.vndbandroid.Vote
import com.booboot.vndbandroid.util.Utils
import com.booboot.vndbandroid.util.image.BlurIfDemoTransform
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.vn_card.view.*

class VNHolder(itemView: View, private val onVnClicked: (View, VN) -> Unit) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    private lateinit var vn: VN

    init {
        itemView.cardView.setOnClickListener(this)
    }

    fun onBind(
            vn: VN,
            vnlist: Vnlist?,
            votelist: Votelist?,
            wishlist: Wishlist?,
            showFullDate: Boolean,
            showRank: Boolean,
            showRating: Boolean,
            showPopularity: Boolean,
            showVoteCount: Boolean
    ) = with(itemView) {
        this@VNHolder.vn = vn

        val titleText = StringBuilder()
        val subtitleText = StringBuilder()
        if (showRank)
            titleText.append("#").append(adapterPosition + 1).append(context.getString(R.string.dash))
        titleText.append(vn.title)
        when {
            showRating -> subtitleText.append(vn.rating).append(" (").append(Vote.getName(vn.rating.toDouble())).append(")")
            showPopularity -> subtitleText.append(vn.popularity).append("%")
            showVoteCount -> subtitleText.append(vn.votecount).append(" votes")
            else -> subtitleText.append(Utils.getDate(vn.released, showFullDate))
        }

        subtitleText.append(context.getString(R.string.bullet))
        if (vn.length > 0)
            subtitleText.append(vn.lengthString())
        else
            subtitleText.append(Utils.getDate(vn.released, true))

        if (vn.image_nsfw && !Preferences.nsfw)
            image.setImageResource(R.drawable.ic_nsfw)
        else
            Picasso.get().load(vn.image).transform(BlurIfDemoTransform(context)).into(image)

        title.text = titleText
        subtitle.text = subtitleText
        statusButton.text = Status.toShortString(vnlist?.status)
        wishlistButton.text = Priority.toShortString(wishlist?.priority)
        votesButton.text = Vote.toShortString(votelist?.vote)
    }

    override fun onClick(v: View?) {
        onVnClicked(itemView, vn)
    }
}