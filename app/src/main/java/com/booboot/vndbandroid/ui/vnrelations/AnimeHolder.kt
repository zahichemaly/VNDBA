package com.booboot.vndbandroid.ui.vnrelations

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.model.vndb.Anime
import kotlinx.android.synthetic.main.anime_card.view.*

class AnimeHolder(itemView: View, private val onAnimeClicked: (Anime) -> Unit) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    private var anime: Anime? = null

    init {
        itemView.cardView.setOnClickListener(this)
    }

    fun onBind(anime: Anime) = with(itemView) {
        this@AnimeHolder.anime = anime

        title.text = anime.title_romaji
        anime.type?.let {
            subtitle.text = String.format("%s%s%s", it, context.getString(R.string.bullet), anime.year)
        } ?: let {
            subtitle.text = anime.year.toString()
        }
    }

    override fun onClick(v: View?) {
        anime?.let { onAnimeClicked(it) }
    }
}