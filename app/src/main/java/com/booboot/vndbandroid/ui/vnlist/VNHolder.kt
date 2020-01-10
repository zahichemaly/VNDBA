package com.booboot.vndbandroid.ui.vnlist

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.extensions.showNsfwImage
import com.booboot.vndbandroid.model.vndb.Label
import com.booboot.vndbandroid.model.vndb.UserList
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.model.vndbandroid.Vote
import com.booboot.vndbandroid.util.Utils
import kotlinx.android.synthetic.main.nsfw_tag.view.*
import kotlinx.android.synthetic.main.vn_card.view.*

class VNHolder(itemView: View, private val onVnClicked: (View, VN) -> Unit) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    private lateinit var vn: VN

    init {
        itemView.cardView.setOnClickListener(this)
    }

    fun onBind(
        vn: VN,
        userList: UserList?,
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
            showRating -> subtitleText.append(vn.rating).append(" (").append(Vote.getName(vn.rating)).append(")")
            showPopularity -> subtitleText.append(vn.popularity).append("%")
            showVoteCount -> subtitleText.append(vn.votecount).append(" votes")
            else -> subtitleText.append(Utils.getDate(vn.released, showFullDate))
        }

        subtitleText.append(context.getString(R.string.bullet))
        if (vn.length ?: 0 > 0)
            subtitleText.append(vn.lengthFull())
        else
            subtitleText.append(Utils.getDate(vn.released, true))

        image.transitionName = "slideshow" + vn.id
        image.showNsfwImage(vn.image, vn.image_nsfw, nsfwTag)

        title.text = titleText
        subtitle.text = subtitleText
        statusButton.text = Label.toShortString(userList?.firstStatus()?.id)
        wishlistButton.text = Label.toShortString(userList?.firstWishlist()?.id)
        votesButton.text = Vote.toShortString(userList?.vote)
    }

    override fun onClick(v: View?) {
        onVnClicked(itemView, vn)
    }
}