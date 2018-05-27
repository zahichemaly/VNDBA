package com.booboot.vndbandroid.ui.slideshow

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.widget.ImageView
import com.booboot.vndbandroid.R
import kotlinx.android.synthetic.main.slideshow_activity.*

class SlideshowActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.slideshow_activity)

        slideshow.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val slideshowAdapter = SlideshowAdapter(scaleType = ImageView.ScaleType.FIT_CENTER)
        slideshow.adapter = slideshowAdapter
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(slideshow)

        val position = intent.getIntExtra(INDEX_ARG, 0)
        val images = intent.getCharSequenceArrayListExtra(IMAGES_ARG)
        slideshowAdapter.images = images.map { it.toString() }
        slideshow.scrollToPosition(position)
    }

    companion object {
        const val IMAGES_ARG = "IMAGES_ARG"
        const val INDEX_ARG = "INDEX_ARG"
    }
}