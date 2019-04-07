package com.booboot.vndbandroid.ui.vnrelations

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.diff.RelationsDiffCallback
import com.booboot.vndbandroid.extensions.log
import com.booboot.vndbandroid.model.vndb.Anime
import com.booboot.vndbandroid.model.vndb.Relation
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.ui.base.BaseAdapter
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class RelationsAdapter(private val onAnimeClicked: (Anime) -> Unit, private val onRelationClicked: (View, Relation, VN?) -> Unit) : BaseAdapter<RecyclerView.ViewHolder>() {
    private var vn: VN = VN()
    private var disposable: Disposable? = null
    var relationsData: RelationsData = RelationsData()
        set(value) {
            disposable?.dispose()
            disposable = Single.fromCallable {
                val diffCallback = RelationsDiffCallback(field, value)
                val diffResult = DiffUtil.calculateDiff(diffCallback)
                Pair(diffCallback, diffResult)
            }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    vn = it.first.newVn ?: return@subscribe
                    field = value
                    it.second.dispatchUpdatesTo(this)
                }, {
                    it.log()
                    vn = value.items.vns[value.vnId] ?: return@subscribe
                    field = value
                    notifyDataSetChanged()
                })
        }

    override fun getItemViewType(position: Int) = RelationsDiffCallback.getItemViewType(position, vn)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.anime_card -> AnimeHolder(view, onAnimeClicked)
            else -> RelationHolder(view, onRelationClicked)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is AnimeHolder) {
            holder.onBind(vn.anime[position], vn)
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