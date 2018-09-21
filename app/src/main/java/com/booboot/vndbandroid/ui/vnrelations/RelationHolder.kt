package com.booboot.vndbandroid.ui.vnrelations

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.booboot.vndbandroid.model.vndb.RELATIONS
import com.booboot.vndbandroid.model.vndb.Relation
import com.booboot.vndbandroid.model.vndb.VN
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.vn_card.view.*

class RelationHolder(itemView: View, private val onClick: (Relation, VN?) -> Unit) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    private var relation: Relation? = null
    private var vn: VN? = null

    init {
        itemView.cardView.setOnClickListener(this)
    }

    fun onBind(relation: Relation, vn: VN?) = with(itemView) {
        this@RelationHolder.relation = relation
        this@RelationHolder.vn = vn

        title.text = relation.title
        subtitle.text = RELATIONS[relation.relation]
        Picasso.get().load(vn?.image).into(image)
    }

    override fun onClick(v: View?) {
        relation?.let { onClick(it, vn) }
    }
}