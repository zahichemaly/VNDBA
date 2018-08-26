package com.booboot.vndbandroid.ui.vnrelations

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.model.vndb.Anime
import com.booboot.vndbandroid.model.vndb.Relation

class RelationsAdapter(private val onClick: (Anime?, Relation?) -> Unit) : RecyclerView.Adapter<RelationHolder>() {
    var anime: List<Anime> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var relations: List<Relation> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RelationHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.relation_item, parent, false)
        return RelationHolder(v, onClick)
    }

    override fun onBindViewHolder(holder: RelationHolder, position: Int) {
        holder.onBind(
            if (position < anime.size) anime[position] else null,
            if (position >= anime.size) relations[position - anime.size] else null
        )
    }

    override fun getItemCount() = relations.size + anime.size
}