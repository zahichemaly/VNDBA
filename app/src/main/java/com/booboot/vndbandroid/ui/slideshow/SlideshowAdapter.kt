package com.booboot.vndbandroid.ui.slideshow

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.extensions.showNsfwImage
import com.booboot.vndbandroid.model.vndb.Screen
import kotlinx.android.synthetic.main.nsfw_tag.view.*
import kotlinx.android.synthetic.main.slideshow_item.view.*

internal class SlideshowAdapter(
    context: Context,
    private val listener: Listener? = null,
    private val scaleType: ImageView.ScaleType = ImageView.ScaleType.CENTER_CROP
) : PagerAdapter() {
    private var mLayoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    var images: List<Screen> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    interface Listener {
        fun onImageClicked(position: Int, images: List<Screen>)
    }

    override fun getCount(): Int = images.size

    override fun isViewFromObject(view: View, any: Any) = view === any

    override fun instantiateItem(container: ViewGroup, position: Int): Any =
        with(mLayoutInflater.inflate(R.layout.slideshow_item, container, false)) {
            imageView.scaleType = scaleType
            imageView.tag = position

            imageView.showNsfwImage(images[position].image, images[position].nsfw, nsfwTag)
            nsfwText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)

            imageView.setOnClickListener {
                listener?.onImageClicked(position, images)
            }

            container.addView(this)
            this
        }

    override fun destroyItem(container: ViewGroup, position: Int, any: Any) {
        container.removeView(any as View)
    }
}