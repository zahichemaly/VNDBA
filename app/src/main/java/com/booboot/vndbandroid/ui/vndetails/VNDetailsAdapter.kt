package com.booboot.vndbandroid.ui.vndetails

import android.view.View
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.model.vndb.Label.Companion.CUSTOM_VOTE
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.custom_vote_item.*

data class CustomVoteItem(
    val customVote: String?,
    val setCustomVote: (String?) -> Unit,
    val _id: Long = CUSTOM_VOTE
) : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.apply {
            inputCustomVote.setText(customVote)
            inputCustomVote.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    setCustomVote(inputCustomVote.text.toString())
                }
            }
        }
    }

    override fun getId() = _id
    override fun getLayout() = R.layout.custom_vote_item
}