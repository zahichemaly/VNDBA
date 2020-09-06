package com.booboot.vndbandroid.ui.vndetails

import android.widget.TextView
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.extensions.preventLineBreak
import com.booboot.vndbandroid.extensions.removeFocus
import com.booboot.vndbandroid.model.vndb.Label.Companion.CUSTOM_VOTE
import com.booboot.vndbandroid.model.vndb.Label.Companion.NOTES_ITEM
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.custom_vote_item.*
import kotlinx.android.synthetic.main.vn_details_notes.*

data class CustomVoteItem(
    val customVote: String?,
    val setCustomVote: (String?) -> Unit
) : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.apply {
            inputCustomVote.setText(customVote)
            inputCustomVote.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    setCustomVote(inputCustomVote.text.toString())
                }
            }
        }
    }

    override fun getId() = CUSTOM_VOTE
    override fun getLayout() = R.layout.custom_vote_item

    override fun onViewDetachedFromWindow(viewHolder: GroupieViewHolder) {
        viewHolder.inputCustomVote?.removeFocus(true)
    }
}

data class NotesItem(val notes: String?, val onSubmit: (String) -> Unit) : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.apply {
            textNotes.preventLineBreak()
            textNotes.setText(notes, TextView.BufferType.EDITABLE)
            textNotes.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    onSubmit(textNotes.text.toString())
                }
            }
        }
    }

    override fun getLayout() = R.layout.vn_details_notes
    override fun getId() = NOTES_ITEM

    override fun onViewDetachedFromWindow(viewHolder: GroupieViewHolder) {
        viewHolder.textNotes?.removeFocus(true)
    }
}