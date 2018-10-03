package com.booboot.vndbandroid.ui.vndetails

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.extensions.onStateChanged
import com.booboot.vndbandroid.extensions.setStatusBarThemeForCollapsingToolbar
import com.booboot.vndbandroid.extensions.toggle
import com.booboot.vndbandroid.model.vndb.AccountItems
import com.booboot.vndbandroid.model.vndb.Screen
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.model.vndbandroid.Priority
import com.booboot.vndbandroid.model.vndbandroid.Status
import com.booboot.vndbandroid.model.vndbandroid.Vote
import com.booboot.vndbandroid.ui.base.BaseActivity
import com.booboot.vndbandroid.ui.slideshow.SlideshowActivity
import com.booboot.vndbandroid.ui.slideshow.SlideshowAdapter
import com.booboot.vndbandroid.util.StopFocusStealingAppBarBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.vn_details_activity.*
import kotlinx.android.synthetic.main.vn_details_bottom_sheet.*
import java.io.Serializable

class VNDetailsActivity : BaseActivity(), SlideshowAdapter.Listener, View.OnClickListener {
    private lateinit var viewModel: VNDetailsViewModel
    private lateinit var slideshowAdapter: SlideshowAdapter
    private lateinit var tabsAdapter: VNDetailsTabsAdapter
    private var vnId: Long = 0
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme_DayNight_NoActionBar_Translucent)
        setContentView(R.layout.vn_details_activity)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        vnId = intent.getLongExtra(EXTRA_VN_ID, 0)
        val vnImage = intent.getStringExtra(EXTRA_SHARED_ELEMENT_COVER)
        val vnImageNsfw = intent.getBooleanExtra(EXTRA_SHARED_ELEMENT_COVER_NSFW, false)

        tabsAdapter = VNDetailsTabsAdapter(supportFragmentManager)
        viewPager.adapter = tabsAdapter
        tabLayout.setupWithViewPager(viewPager)

        appBarLayout.setStatusBarThemeForCollapsingToolbar(this, collapsingToolbar, content)

        slideshowAdapter = SlideshowAdapter(this, this, scaleType = ImageView.ScaleType.CENTER_CROP)
        slideshow.adapter = slideshowAdapter
        vnImage?.let {
            slideshowAdapter.images = mutableListOf(Screen(image = it, nsfw = vnImageNsfw))
        }

        /* Bottom sheet */
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetHeader.setOnClickListener(this)
        bottomSheetBehavior.onStateChanged(
            { iconArrow.setImageResource(R.drawable.ic_keyboard_arrow_down_white_24dp) },
            { iconArrow.setImageResource(R.drawable.ic_keyboard_arrow_up_white_24dp) }
        )
        (appBarLayout.layoutParams as? CoordinatorLayout.LayoutParams)?.behavior = StopFocusStealingAppBarBehavior(bottomSheet)

        viewModel = ViewModelProviders.of(this).get(VNDetailsViewModel::class.java)
        viewModel.loadingData.observe(this, Observer { showLoading(it) })
        viewModel.vnData.observe(this, Observer { showVn(it) })
        viewModel.accountData.observe(this, Observer { showAccount(it) })
        viewModel.errorData.observe(this, Observer { showError(it) })
        viewModel.loadVn(vnId, false)
        viewModel.loadAccount(false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> supportFinishAfterTransition()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showVn(vn: VN?) {
        vn ?: return
        supportActionBar?.title = vn.title

        val screens = vn.image?.let { mutableListOf(Screen(image = it, nsfw = vn.image_nsfw)) } ?: mutableListOf()
        screens.addAll(vn.screens)
        slideshowAdapter.images = screens
        numberOfImages.text = String.format("x%d", screens.size)

        tabsAdapter.vn = vn
    }

    private fun showAccount(items: AccountItems?) {
        items ?: return

        val status = Status.toString(items.vnlist[vnId]?.status)
        val priority = Priority.toString(items.wishlist[vnId]?.priority)
        val vote = Vote.toShortString(items.votelist[vnId]?.vote, null)

        textAddToList.text = when {
            status == null && priority == null && vote == null -> getString(R.string.add_to_your_lists)
            else -> getString(R.string.edit_your_lists)
        }

        textStatus.text = status
        textWishlist.text = priority
        votesButton.text = vote ?: getString(R.string.dash)

        iconStatus.toggle(status != null)
        textStatus.toggle(status != null)
        iconWishlist.toggle(priority != null)
        textWishlist.toggle(priority != null)

        votesButton.background = ContextCompat.getDrawable(this, Vote.getDrawableColor10(items.votelist[vnId]?.vote))
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.bottomSheetHeader -> bottomSheetBehavior.toggle()
        }
    }

    override fun onImageClicked(position: Int, images: List<Screen>) {
        val intent = Intent(this, SlideshowActivity::class.java)
        intent.putExtra(SlideshowActivity.INDEX_ARG, position)
        intent.putExtra(SlideshowActivity.IMAGES_ARG, images as Serializable)
        startActivity(intent)
    }

    companion object {
        const val EXTRA_VN_ID = "EXTRA_VN_ID"
        const val EXTRA_SHARED_ELEMENT_COVER = "EXTRA_SHARED_ELEMENT_COVER"
        const val EXTRA_SHARED_ELEMENT_COVER_NSFW = "EXTRA_SHARED_ELEMENT_COVER_NSFW"
    }
}