package com.booboot.vndbandroid.ui.slideshow

import android.Manifest
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.model.vndbandroid.FileAction
import com.booboot.vndbandroid.ui.base.BaseActivity
import kotlinx.android.synthetic.main.slideshow_activity.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class SlideshowActivity : BaseActivity(), ViewPager.OnPageChangeListener {
    private lateinit var viewModel: SlideshowViewModel

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

        viewModel = ViewModelProviders.of(this).get(SlideshowViewModel::class.java)
        viewModel.errorData.observe(this, Observer { showError(it) })
        viewModel.loadingData.observe(this, Observer { showLoading(it) })
        viewModel.fileData.observe(this, Observer { fileIntent(it) })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.slideshow_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.action_share -> downloadScreenshot(SHARE_SCREENSHOT_PERMISSION)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    @AfterPermissionGranted(DOWNLOAD_SCREENSHOT_PERMISSION)
    private fun downloadScreenshot() {
        downloadScreenshot(DOWNLOAD_SCREENSHOT_PERMISSION)
    }

    @AfterPermissionGranted(SHARE_SCREENSHOT_PERMISSION)
    private fun shareScreenshot() {
        downloadScreenshot(SHARE_SCREENSHOT_PERMISSION)
    }

    private fun downloadScreenshot(action: Int) {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            val imageView = slideshow.findViewWithTag<ImageView>(slideshow.currentItem)
            val bitmap = (imageView.drawable as? BitmapDrawable)?.bitmap
            viewModel.downloadScreenshot(bitmap, action, externalCacheDir)
        } else {
            EasyPermissions.requestPermissions(this, String.format(getString(R.string.share_screenshot_rationale), getString(R.string.app_name)),
                action, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    private fun fileIntent(fileAction: FileAction?) {
        if (fileAction == null) return

        when (fileAction.action) {
            DOWNLOAD_SCREENSHOT_PERMISSION -> {
                // TODO download action menu + download notification with preview like Slide

            }
            SHARE_SCREENSHOT_PERMISSION -> {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "image/*"
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(fileAction.file))
                startActivity(Intent.createChooser(intent, getString(R.string.action_share)))
            }
        }
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
        const val DOWNLOAD_SCREENSHOT_PERMISSION = 1001
        const val SHARE_SCREENSHOT_PERMISSION = 1002
    }
}