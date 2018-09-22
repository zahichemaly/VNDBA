package com.booboot.vndbandroid.ui.vnrelations

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.model.vndb.Anime
import com.booboot.vndbandroid.model.vndb.Relation
import com.booboot.vndbandroid.model.vndb.VN

class RelationsAdapter(private val onAnimeClicked: (Anime) -> Unit, private val onRelationClicked: (Relation, VN?) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var relationsData: RelationsData = RelationsData()
        set(value) {
            vn = value.items.vns[value.vnId] ?: return
            field = value
            notifyDataSetChanged()
        }
    private var vn: VN = VN()

    override fun getItemViewType(position: Int) = if (position < vn.anime.size) {
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
            holder.onBind(vn.anime[position])
        } else if (holder is RelationHolder) {
            val relation = vn.relations[position - vn.anime.size]
            holder.onBind(
                relation,
                relationsData.relations[relation.id],
                relationsData.items.vnlist[relation.id],
                relationsData.items.votelist[relation.id],
                relationsData.items.wishlist[relation.id]
            )
        }
    }

    override fun getItemCount() = vn.relations.size + vn.anime.size
}