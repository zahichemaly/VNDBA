package com.booboot.vndbandroid.ui.vnrelations

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.diff.RelationsDiffCallback
import com.booboot.vndbandroid.model.vndb.Anime
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.ui.base.BaseAdapter

class RelationsAdapter(
    private val onAnimeClicked: (Anime) -> Unit,
    private val onRelationClicked: (View, VN?) -> Unit
) : BaseAdapter<RecyclerView.ViewHolder>() {
    var relationsData: RelationsData = RelationsData()
        set(value) {
            field = value
            onUpdateInternal()
            value.diffResult?.dispatchUpdatesTo(this@RelationsAdapter) ?: notifyChanged()
        }

    override fun getItemViewType(position: Int) = RelationsDiffCallback.getItemViewType(position, relationsData.vn)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.anime_card -> AnimeHolder(view, onAnimeClicked)
            else -> RelationHolder(view, onRelationClicked)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is AnimeHolder) {
            holder.onBind(relationsData.vn.anime[position], relationsData.vn)
        } else if (holder is RelationHolder) {
            val relation = relationsData.vn.relations[position - relationsData.vn.anime.size]
            holder.onBind(
                relation,
                relationsData.relations[relation.id],
                relationsData.items.vnlist[relation.id],
                relationsData.items.votelist[relation.id],
                relationsData.items.wishlist[relation.id]
            )
        }
    }

    override fun getItemCount() = relationsData.vn.relations.size + relationsData.vn.anime.size
}