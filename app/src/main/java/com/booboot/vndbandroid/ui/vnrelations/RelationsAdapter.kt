package com.booboot.vndbandroid.ui.vnrelations

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.model.vndb.Anime
import com.booboot.vndbandroid.model.vndb.Relation
import com.booboot.vndbandroid.model.vndb.VN

class RelationsAdapter(private val onAnimeClicked: (Anime) -> Unit, private val onRelationClicked: (Relation, VN?) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var anime: List<Anime> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var vnWithRelations: VNWithRelations = VNWithRelations()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemViewType(position: Int) = if (position < anime.size) {
        R.layout.anime_card
    } else {
        R.layout.vn_card
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.anime_card -> AnimeHolder(view, onAnimeClicked)
            else -> RelationHolder(view, onRelationClicked)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is AnimeHolder) {
            holder.onBind(anime[position])
        } else if (holder is RelationHolder) {
            val relation = vnWithRelations.vn.relations[position - anime.size]
            holder.onBind(relation, vnWithRelations.relations[relation.id])
        }
    }

    override fun getItemCount() = vnWithRelations.vn.relations.size + anime.size
}