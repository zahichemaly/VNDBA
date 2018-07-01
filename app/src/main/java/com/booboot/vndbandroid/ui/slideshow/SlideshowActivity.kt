package com.booboot.vndbandroid.ui.slideshow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import com.booboot.vndbandroid.R
import kotlinx.android.synthetic.main.slideshow_activity.*

class SlideshowActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.slideshow_activity)

        val position = intent.getIntExtra(INDEX_ARG, 0)
        val images = intent.getCharSequenceArrayListExtra(IMAGES_ARG)

        val slideshowAdapter = SlideshowAdapter(this, scaleType = ImageView.ScaleType.FIT_CENTER)
        slideshow.adapter = slideshowAdapter

        slideshowAdapter.images = images.map { it.toString() }
        slideshow.currentItem = position
    }

    companion object {
        const val IMAGES_ARG = "IMAGES_ARG"
        const val INDEX_ARG = "INDEX_ARG"
    }
}