package com.booboot.vndbandroid.ui.slideshow

import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.booboot.vndbandroid.R
import kotlinx.android.synthetic.main.slideshow_activity.*

class SlideshowActivity : AppCompatActivity(), ViewPager.OnPageChangeListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.slideshow_activity)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val position = intent.getIntExtra(INDEX_ARG, 0)
        val images = intent.getCharSequenceArrayListExtra(IMAGES_ARG)

        val slideshowAdapter = SlideshowAdapter(this, scaleType = ImageView.ScaleType.FIT_CENTER)
        slideshow.adapter = slideshowAdapter
        slideshow.addOnPageChangeListener(this)

        slideshowAdapter.images = images.map { it.toString() }
        slideshow.currentItem = position
        onPageSelected(position)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        supportActionBar?.title = String.format("%d/%d", position + 1, slideshow.adapter?.count)
    }

    companion object {
        const val IMAGES_ARG = "IMAGES_ARG"
        const val INDEX_ARG = "INDEX_ARG"
    }
}