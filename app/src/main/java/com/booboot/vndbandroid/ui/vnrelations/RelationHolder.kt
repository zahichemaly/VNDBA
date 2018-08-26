package com.booboot.vndbandroid.ui.vnrelations

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.model.vndb.Anime
import com.booboot.vndbandroid.model.vndb.RELATIONS
import com.booboot.vndbandroid.model.vndb.Relation
import kotlinx.android.synthetic.main.relation_item.view.*

class RelationHolder(itemView: View, private val onClick: (Anime?, Relation?) -> Unit) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    private var anime: Anime? = null
    private var relation: Relation? = null

    init {
        itemView.cardView.setOnClickListener(this)
    }

    fun onBind(anime: Anime?, relation: Relation?) = with(itemView) {
        this@RelationHolder.anime = anime
        this@RelationHolder.relation = relation

        anime?.let {
            title.text = it.title_romaji
            subtitle.text = String.format("%s%s%s", it.type, context.getString(R.string.bullet), it.year)
        } ?: relation?.let {
            title.text = it.title
            subtitle.text = RELATIONS[it.relation]
        }
    }

    override fun onClick(v: View?) {
        onClick(anime, relation)
    }
}