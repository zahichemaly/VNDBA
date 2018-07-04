package com.booboot.vndbandroid.ui.slideshow

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.booboot.vndbandroid.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.slideshow_item.view.*

internal class SlideshowAdapter(
    context: Context,
    private val listener: Listener? = null,
    private val scaleType: ImageView.ScaleType = ImageView.ScaleType.CENTER_CROP
) : PagerAdapter() {
    private var mLayoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    var images: List<String> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    interface Listener {
        fun onImageClicked(position: Int, images: List<String>)
    }

    override fun getCount(): Int = images.size

    override fun isViewFromObject(view: View, any: Any) = view === any

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = mLayoutInflater.inflate(R.layout.slideshow_item, container, false)

        itemView.imageView.scaleType = scaleType
        Picasso.get().load(images[position]).into(itemView.imageView)
        itemView.imageView.tag = position

        itemView.setOnClickListener {
            listener?.onImageClicked(position, images)
        }

        container.addView(itemView)

        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, any: Any) {
        container.removeView(any as View)
    }
}