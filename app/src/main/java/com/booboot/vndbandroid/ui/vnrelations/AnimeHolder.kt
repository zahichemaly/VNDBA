package com.booboot.vndbandroid.ui.vnrelations

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.model.vndb.Anime
import com.booboot.vndbandroid.model.vndb.VN
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.anime_card.view.*

class AnimeHolder(itemView: View, private val onAnimeClicked: (Anime) -> Unit) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    private var anime: Anime? = null

    init {
        itemView.cardView.setOnClickListener(this)
    }

    fun onBind(anime: Anime, vn: VN) = with(itemView) {
        this@AnimeHolder.anime = anime

        title.text = anime.title_romaji
        subtitle.text = String.format("%s%s%s", context.getString(R.string.anime), context.getString(R.string.bullet), anime.year)
        Picasso.get().load(vn.image).into(image)
    }

    override fun onClick(v: View?) {
        anime?.let { onAnimeClicked(it) }
    }
}