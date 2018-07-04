package com.booboot.vndbandroid.ui.slideshow

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap.CompressFormat
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.viewpager.widget.ViewPager
import com.booboot.vndbandroid.R
import kotlinx.android.synthetic.main.slideshow_activity.*
import com.booboot.vndbandroid.ui.base.BaseActivity
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.io.FileOutputStream

class SlideshowActivity : BaseActivity(), ViewPager.OnPageChangeListener {
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.slideshow_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.action_share -> shareScreenshot()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    @AfterPermissionGranted(DOWNLOAD_SCREENSHOT_PERMISSION)
    private fun shareScreenshot() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            val imageView = slideshow.findViewWithTag<ImageView>(slideshow.currentItem)
            val bitmap = (imageView.drawable as? BitmapDrawable)?.bitmap

            bitmap?.let {
                val root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val filename = String.format("IMAGE_%d.jpg", System.currentTimeMillis())
                val file = File(root, filename)

                // TODO put the image saving in a download function + download action menu + download notification with preview like Slide
                try {
                    file.createNewFile()
                    val ostream = FileOutputStream(file)
                    bitmap.compress(CompressFormat.JPEG, 100, ostream)
                    ostream.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                    showError("Error while saving the image.")
                    return
                }

                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "image/*"
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file))
                startActivity(Intent.createChooser(intent, getString(R.string.action_share)))
            } ?: let { showError("The image is not ready to be shared yet.") }
        } else {
            EasyPermissions.requestPermissions(
                    this,
                    String.format(getString(R.string.share_screenshot_rationale), getString(R.string.app_name)),
                    DOWNLOAD_SCREENSHOT_PERMISSION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
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
    }
}