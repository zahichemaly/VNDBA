package com.booboot.vndbandroid.ui.slideshow

import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.booboot.vndbandroid.App
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.extensions.home
import com.booboot.vndbandroid.extensions.observeNonNull
import com.booboot.vndbandroid.extensions.observeOnce
import com.booboot.vndbandroid.extensions.setupStatusBar
import com.booboot.vndbandroid.extensions.setupToolbar
import com.booboot.vndbandroid.model.vndb.Screen
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.model.vndbandroid.FileAction
import com.booboot.vndbandroid.service.ScreenshotNotificationService
import com.booboot.vndbandroid.ui.base.BaseFragment
import com.booboot.vndbandroid.util.Notifications
import kotlinx.android.synthetic.main.slideshow_fragment.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class SlideshowFragment : BaseFragment<SlideshowViewModel>(), ViewPager.OnPageChangeListener {
    override val layout: Int = R.layout.slideshow_fragment
    private lateinit var slideshowAdapter: SlideshowAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbar()
        setupStatusBar(true)

        viewModel = ViewModelProvider(this).get(SlideshowViewModel::class.java)
        viewModel.restoreState(savedInstanceState)
        viewModel.errorData.observeOnce(this, ::showError)
        viewModel.loadingData.observeNonNull(this, ::showLoading)
        viewModel.fileData.observeOnce(this, ::launchActionForImage)
        viewModel.vnData.observeNonNull(this, ::showVn)

        arguments?.let { arguments ->
            viewModel.position = if (viewModel.position < 0) {
                SlideshowFragmentArgs.fromBundle(arguments).position
            } else viewModel.position
            viewModel.vnId = SlideshowFragmentArgs.fromBundle(arguments).vnId
        }

        slideshowAdapter = SlideshowAdapter(layoutInflater, scaleType = ImageView.ScaleType.FIT_CENTER)
        slideshow.adapter = slideshowAdapter
        slideshow.addOnPageChangeListener(this)

        viewModel.loadVn(false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.slideshow_fragment, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_download -> downloadScreenshot(DOWNLOAD_SCREENSHOT_PERMISSION)
            R.id.action_share -> downloadScreenshot(SHARE_SCREENSHOT_PERMISSION)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showVn(vn: VN) {
        val images = listOfNotNull(
            vn.image?.let { Screen(image = it, nsfw = vn.image_nsfw) },
            *vn.screens.toTypedArray()
        )
        slideshowAdapter.images = images
        slideshow.setCurrentItem(viewModel.position, false)
        onPageSelected(viewModel.position)
    }

    override fun showError(message: String) {
        super.onError()
        home()?.viewModel?.onError(Throwable(message))
        findNavController().popBackStack()
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
        val activity = home() ?: return

        if (EasyPermissions.hasPermissions(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            val imageView = slideshow.findViewWithTag<ImageView>(slideshow.currentItem)
            val bitmap = (imageView.drawable as? BitmapDrawable)?.bitmap
            viewModel.downloadScreenshot(bitmap, action) //, directory)
        } else {
            EasyPermissions.requestPermissions(this, String.format(getString(R.string.share_screenshot_rationale), getString(R.string.app_name)),
                action, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    private fun launchActionForImage(fileAction: FileAction?) {
        val activity = home() ?: return
        fileAction ?: return

        val shareIntent = getFileIntent(fileAction.uri).apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_UID, REQUEST_CODE_SHARE)
        }

        when (fileAction.action) {
            DOWNLOAD_SCREENSHOT_PERMISSION -> {
                val mainIntent = getFileIntent(fileAction.uri).apply {
                    data = fileAction.uri
                    action = Intent.ACTION_VIEW
                }
                shareIntent.setClass(activity, ScreenshotNotificationService::class.java)
                val deleteIntent = getFileIntent(fileAction.uri).apply {
                    setClass(activity, ScreenshotNotificationService::class.java)
                    putExtra(Intent.EXTRA_ORIGINATING_URI, fileAction.uri)
                    putExtra(Intent.EXTRA_UID, REQUEST_CODE_DELETE)
                }

                val pendingFlags = PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_ONE_SHOT
                val mainPendingIntent = PendingIntent.getActivity(App.context, REQUEST_CODE_VIEW, mainIntent, pendingFlags)
                val sharePendingIntent = PendingIntent.getService(App.context, REQUEST_CODE_SHARE, shareIntent, pendingFlags)
                val deletePendingIntent = PendingIntent.getService(App.context, REQUEST_CODE_DELETE, deleteIntent, pendingFlags)

                val notificationManager = activity.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                val notif = NotificationCompat.Builder(App.context, Notifications.DEFAULT_CHANNEL_ID)
                    .setContentIntent(mainPendingIntent)
                    .setContentTitle("Image saved.")
                    .setSmallIcon(R.drawable.ic_logo_transparent)
                    .setLargeIcon(fileAction.bitmap)
                    .addAction(R.drawable.ic_share_white_24dp, getString(R.string.action_share), sharePendingIntent)
                    .addAction(R.drawable.ic_delete_forever_black_24dp, getString(R.string.action_delete), deletePendingIntent)
                    .setAutoCancel(true)
                    .setStyle(NotificationCompat.BigPictureStyle().bigPicture(fileAction.bitmap))
                    .build()
                notificationManager.notify(Notifications.IMAGE_DOWNLOADED_NOTIFICATION_ID, notif)
            }

            SHARE_SCREENSHOT_PERMISSION -> startActivity(Intent.createChooser(shareIntent, getString(R.string.action_share)))
        }
    }

    private fun getFileIntent(uri: Uri) = Intent().apply {
        type = home()?.contentResolver?.getType(uri)
        action = System.nanoTime().toString()
        putExtra(Intent.EXTRA_STREAM, uri)
        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        viewModel.position = position
        home()?.supportActionBar?.title = String.format("%d/%d", position + 1, slideshow.adapter?.count ?: 0)
    }
}

const val DOWNLOAD_SCREENSHOT_PERMISSION = 1001
const val SHARE_SCREENSHOT_PERMISSION = 1002
const val REQUEST_CODE_VIEW = 100
const val REQUEST_CODE_SHARE = 101
const val REQUEST_CODE_DELETE = 102