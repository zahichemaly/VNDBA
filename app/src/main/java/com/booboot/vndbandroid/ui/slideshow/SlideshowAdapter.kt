package com.booboot.vndbandroid.ui.slideshow

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.booboot.vndbandroid.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.slideshow_item.view.*

class SlideshowHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

class SlideshowAdapter(
        private val listener: Listener? = null,
        private val scaleType: ImageView.ScaleType = ImageView.ScaleType.CENTER_CROP
) : RecyclerView.Adapter<SlideshowHolder>() {
    var images: List<String> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    interface Listener {
        fun onImageClicked(position: Int, images: List<String>)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlideshowHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.slideshow_item, parent, false)
        return SlideshowHolder(view)
    }

    override fun onBindViewHolder(viewHolder: SlideshowHolder, i: Int) {
        viewHolder.itemView.imageView.scaleType = scaleType
        Picasso.get().load(images[i]).into(viewHolder.itemView.imageView)

        viewHolder.itemView.setOnClickListener {
            listener?.onImageClicked(viewHolder.adapterPosition, images)
        }
    }
}