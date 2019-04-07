package com.booboot.vndbandroid.ui.slideshow

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
    private val layoutInflater: LayoutInflater,
    private val onImageClicked: (Int) -> Unit = {},
    private val scaleType: ImageView.ScaleType = ImageView.ScaleType.CENTER_CROP
) : PagerAdapter() {
    var images: List<Screen> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getCount(): Int = images.size

    override fun isViewFromObject(view: View, any: Any) = view === any

    override fun instantiateItem(container: ViewGroup, position: Int): Any =
        with(layoutInflater.inflate(R.layout.slideshow_item, container, false)) {
            imageView.scaleType = scaleType
            imageView.tag = position

            imageView.showNsfwImage(images[position].image, images[position].nsfw, nsfwTag)
            nsfwText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)

            imageView.setOnClickListener {
                onImageClicked(position)
            }

            container.addView(this)
            this
        }

    override fun destroyItem(container: ViewGroup, position: Int, any: Any) {
        container.removeView(any as View)
    }
}