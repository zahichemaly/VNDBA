package com.booboot.vndbandroid.ui.vndetails

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.extensions.onStateChanged
import com.booboot.vndbandroid.extensions.onSubmitListener
import com.booboot.vndbandroid.extensions.preventLineBreak
import com.booboot.vndbandroid.extensions.selectIf
import com.booboot.vndbandroid.extensions.setStatusBarThemeForCollapsingToolbar
import com.booboot.vndbandroid.extensions.toggle
import com.booboot.vndbandroid.model.vndb.AccountItems
import com.booboot.vndbandroid.model.vndb.Screen
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.model.vndbandroid.EXTRA_ERROR_MESSAGE
import com.booboot.vndbandroid.model.vndbandroid.Priority
import com.booboot.vndbandroid.model.vndbandroid.RESULT_ERROR
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

class VNDetailsActivity : BaseActivity(), SlideshowAdapter.Listener, View.OnClickListener, View.OnFocusChangeListener {
    private lateinit var viewModel: VNDetailsViewModel
    private lateinit var slideshowAdapter: SlideshowAdapter
    private lateinit var tabsAdapter: VNDetailsTabsAdapter
    private var vnId: Long = 0
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private val votelistViews by lazy { listOf(iconContentVote, textContentVote, flexboxVote, buttonRemoveVote) }
    private val wishlistViews by lazy { listOf(iconContentWishlist, textContentWishlist, flexboxWishlist, buttonRemoveWishlist) }
    private val bottomSheetButtons by lazy {
        listOf(textNotes as View, buttonPlaying, buttonFinished, buttonStalled, buttonDropped, buttonUnknown, buttonRemoveStatus,
            buttonVote1, buttonVote2, buttonVote3, buttonVote4, buttonVote5, buttonVote6, buttonVote7, buttonVote8, buttonVote9, buttonVote10, inputCustomVote, buttonRemoveVote,
            buttonWishlistHigh, buttonWishlistMedium, buttonWishlistLow, buttonWishlistBlacklist, buttonRemoveWishlist)
    }

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
        textNotes.preventLineBreak()
        bottomSheetButtons.forEach { it.setOnClickListener(this@VNDetailsActivity) }
        textNotes.onFocusChangeListener = this
        inputCustomVote.onFocusChangeListener = this
        textNotes.onSubmitListener { textNotes.clearFocus() }
        inputCustomVote.onSubmitListener { inputCustomVote.clearFocus() }

        viewModel = ViewModelProviders.of(this).get(VNDetailsViewModel::class.java)
        viewModel.loadingData.observe(this, Observer { showLoading(it) })
        viewModel.vnData.observe(this, Observer { showVn(it) })
        viewModel.accountData.observe(this, Observer { showAccount(it) })
        viewModel.errorData.observe(this, Observer { showError(it) })
        viewModel.initErrorData.observe(this, Observer { onInitError(it) })
        viewModel.loadVn(vnId, false)
        viewModel.loadAccount(false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
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

        val vnlist = items.vnlist[vnId]
        val wishlist = items.wishlist[vnId]
        val votelist = items.votelist[vnId]

        val status = Status.toString(vnlist?.status)
        val priority = Priority.toString(wishlist?.priority)
        val vote = Vote.toShortString(votelist?.vote, null)

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

        votesButton.background = ContextCompat.getDrawable(this, Vote.getDrawableColor10(votelist?.vote))

        textNotes.setText(vnlist?.notes, TextView.BufferType.EDITABLE)
        buttonPlaying.selectIf(vnlist?.status == Status.PLAYING)
        buttonFinished.selectIf(vnlist?.status == Status.FINISHED)
        buttonStalled.selectIf(vnlist?.status == Status.STALLED)
        buttonDropped.selectIf(vnlist?.status == Status.DROPPED)
        buttonUnknown.selectIf(vnlist?.status == Status.UNKNOWN)
        buttonVote1.selectIf(votelist?.vote == 10)
        buttonVote2.selectIf(votelist?.vote == 20)
        buttonVote3.selectIf(votelist?.vote == 30)
        buttonVote4.selectIf(votelist?.vote == 40)
        buttonVote5.selectIf(votelist?.vote == 50)
        buttonVote6.selectIf(votelist?.vote == 60)
        buttonVote7.selectIf(votelist?.vote == 70)
        buttonVote8.selectIf(votelist?.vote == 80)
        buttonVote9.selectIf(votelist?.vote == 90)
        buttonVote10.selectIf(votelist?.vote == 100)
        inputCustomVote.setText(if (votelist?.vote?.rem(10) == 0) null else Vote.toShortString(votelist?.vote, null))
        buttonWishlistHigh.selectIf(wishlist?.priority == Priority.HIGH)
        buttonWishlistMedium.selectIf(wishlist?.priority == Priority.MEDIUM)
        buttonWishlistLow.selectIf(wishlist?.priority == Priority.LOW)
        buttonWishlistBlacklist.selectIf(wishlist?.priority == Priority.BLACKLIST)

        votelistViews.forEach { it?.toggle(wishlist == null) }
        wishlistViews.forEach { it?.toggle(votelist == null) }
    }

    override fun showLoading(loading: Int?) {
        super.showLoading(loading)
        if (loading == null) return
        bottomSheetButtons.forEach { it.isEnabled = loading <= 0 }
    }

    private fun onInitError(message: String?) {
        message ?: return
        setResult(RESULT_ERROR, Intent().apply {
            putExtra(EXTRA_ERROR_MESSAGE, message)
        })
        finish()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.bottomSheetHeader -> bottomSheetBehavior.toggle()

            R.id.buttonPlaying -> viewModel.setStatus(Status.PLAYING)
            R.id.buttonFinished -> viewModel.setStatus(Status.FINISHED)
            R.id.buttonStalled -> viewModel.setStatus(Status.STALLED)
            R.id.buttonDropped -> viewModel.setStatus(Status.DROPPED)
            R.id.buttonUnknown -> viewModel.setStatus(Status.UNKNOWN)
            R.id.buttonRemoveStatus -> viewModel.removeVnlist()

            R.id.buttonVote1 -> viewModel.setVote(10)
            R.id.buttonVote2 -> viewModel.setVote(20)
            R.id.buttonVote3 -> viewModel.setVote(30)
            R.id.buttonVote4 -> viewModel.setVote(40)
            R.id.buttonVote5 -> viewModel.setVote(50)
            R.id.buttonVote6 -> viewModel.setVote(60)
            R.id.buttonVote7 -> viewModel.setVote(70)
            R.id.buttonVote8 -> viewModel.setVote(80)
            R.id.buttonVote9 -> viewModel.setVote(90)
            R.id.buttonVote10 -> viewModel.setVote(100)
            R.id.buttonRemoveVote -> viewModel.removeVotelist()

            R.id.buttonWishlistHigh -> viewModel.setPriority(Priority.HIGH)
            R.id.buttonWishlistMedium -> viewModel.setPriority(Priority.MEDIUM)
            R.id.buttonWishlistLow -> viewModel.setPriority(Priority.LOW)
            R.id.buttonWishlistBlacklist -> viewModel.setPriority(Priority.BLACKLIST)
            R.id.buttonRemoveWishlist -> viewModel.removeWishlist()
        }
    }

    override fun onFocusChange(view: View, hasFocus: Boolean) {
        if (hasFocus) return

        when (view.id) {
            R.id.textNotes -> viewModel.setNotes(textNotes.text.toString())
            R.id.inputCustomVote -> viewModel.setCustomVote(inputCustomVote.text.toString())
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