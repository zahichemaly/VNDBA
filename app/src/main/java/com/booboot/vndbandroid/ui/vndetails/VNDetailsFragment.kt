package com.booboot.vndbandroid.ui.vndetails

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.extensions.home
import com.booboot.vndbandroid.extensions.observe
import com.booboot.vndbandroid.extensions.observeOnce
import com.booboot.vndbandroid.extensions.onStateChanged
import com.booboot.vndbandroid.extensions.onSubmitListener
import com.booboot.vndbandroid.extensions.postponeEnterTransitionIfExists
import com.booboot.vndbandroid.extensions.preventLineBreak
import com.booboot.vndbandroid.extensions.replaceOnTabSelectedListener
import com.booboot.vndbandroid.extensions.selectIf
import com.booboot.vndbandroid.extensions.setStatusBarThemeForCollapsingToolbar
import com.booboot.vndbandroid.extensions.setupStatusBar
import com.booboot.vndbandroid.extensions.setupToolbar
import com.booboot.vndbandroid.extensions.toggle
import com.booboot.vndbandroid.model.vndb.AccountItems
import com.booboot.vndbandroid.model.vndb.Screen
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.model.vndbandroid.Priority
import com.booboot.vndbandroid.model.vndbandroid.Status
import com.booboot.vndbandroid.model.vndbandroid.Vote
import com.booboot.vndbandroid.ui.base.BaseFragment
import com.booboot.vndbandroid.ui.slideshow.SlideshowActivity
import com.booboot.vndbandroid.ui.slideshow.SlideshowAdapter
import com.booboot.vndbandroid.util.StopFocusStealingAppBarBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.vn_details_bottom_sheet.*
import kotlinx.android.synthetic.main.vn_details_fragment.*
import java.io.Serializable

class VNDetailsFragment : BaseFragment<VNDetailsViewModel>(), SlideshowAdapter.Listener, TabLayout.OnTabSelectedListener, View.OnClickListener, View.OnFocusChangeListener {
    override val layout: Int = R.layout.vn_details_fragment
    private lateinit var slideshowAdapter: SlideshowAdapter
    private lateinit var tabsAdapter: VNDetailsTabsAdapter

    private var vnId: Long = 0

    lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private val votelistViews by lazy { listOf(iconContentVote, textContentVote, flexboxVote, buttonRemoveVote) }
    private val wishlistViews by lazy { listOf(iconContentWishlist, textContentWishlist, flexboxWishlist, buttonRemoveWishlist) }
    private val bottomSheetButtons by lazy {
        listOf(textNotes as View, buttonPlaying, buttonFinished, buttonStalled, buttonDropped, buttonUnknown, buttonRemoveStatus,
            buttonVote1, buttonVote2, buttonVote3, buttonVote4, buttonVote5, buttonVote6, buttonVote7, buttonVote8, buttonVote9, buttonVote10, inputCustomVote, buttonRemoveVote,
            buttonWishlistHigh, buttonWishlistMedium, buttonWishlistLow, buttonWishlistBlacklist, buttonRemoveWishlist)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val activity = home() ?: return

        setupStatusBar(true)
        setupToolbar()
        appBarLayout.setStatusBarThemeForCollapsingToolbar(activity, collapsingToolbar, toolbar, content)

        arguments?.let { arguments ->
            vnId = VNDetailsFragmentArgs.fromBundle(arguments).vnId
            val vnImage = VNDetailsFragmentArgs.fromBundle(arguments).vnImage
            val vnImageNsfw = VNDetailsFragmentArgs.fromBundle(arguments).vnImageNsfw

            slideshowAdapter = SlideshowAdapter(activity, this, scaleType = ImageView.ScaleType.CENTER_CROP)
            slideshow.adapter = slideshowAdapter

            vnImage?.let {
                slideshowAdapter.images = mutableListOf(Screen(image = vnImage, nsfw = vnImageNsfw))
            }
        }

        viewModel = ViewModelProviders.of(this).get(VNDetailsViewModel::class.java)
        viewModel.restoreState(savedInstanceState)
        viewModel.accountData = home()?.viewModel?.accountData ?: return
        viewModel.loadingData.observe(this, ::showLoading)
        viewModel.vnData.observe(this, ::showVn)
        home()?.viewModel?.accountData?.observe(this, ::showAccount)
        viewModel.errorData.observeOnce(this, ::showError)
        viewModel.initErrorData.observeOnce(this, ::onInitError)

        tabsAdapter = VNDetailsTabsAdapter(childFragmentManager)
        viewPager.adapter = tabsAdapter
        tabLayout.setupWithViewPager(viewPager)
        postponeEnterTransitionIfExists(viewModel)

        /* Bottom sheet */
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetHeader.setOnClickListener(this)
        bottomSheetBehavior.onStateChanged(
            onCollapsed = { iconArrow.setImageResource(R.drawable.ic_keyboard_arrow_down_white_24dp) },
            onExpanded = { iconArrow.setImageResource(R.drawable.ic_keyboard_arrow_up_white_24dp) }
        )
        (appBarLayout.layoutParams as? CoordinatorLayout.LayoutParams)?.behavior = StopFocusStealingAppBarBehavior(bottomSheet)
        textNotes.preventLineBreak()
        bottomSheetButtons.forEach { it.setOnClickListener(this@VNDetailsFragment) }
        textNotes.onFocusChangeListener = this
        inputCustomVote.onFocusChangeListener = this
        textNotes.onSubmitListener { textNotes.clearFocus() }
        inputCustomVote.onSubmitListener { inputCustomVote.clearFocus() }

        slideshow.transitionName = "slideshow$vnId"

        viewModel.loadVn(vnId, false)
    }

    private fun showVn(vn: VN?) {
        vn ?: return
        home()?.supportActionBar?.title = vn.title

        val screens = vn.image?.let { mutableListOf(Screen(image = it, nsfw = vn.image_nsfw)) } ?: mutableListOf()
        screens.addAll(vn.screens)
        slideshowAdapter.images = screens
        numberOfImages.text = String.format("x%d", screens.size)

        tabsAdapter.vn = vn
        if (viewModel.currentPage >= 0) viewPager.currentItem = viewModel.currentPage
        tabLayout.replaceOnTabSelectedListener(this)
    }

    private fun showAccount(items: AccountItems?) {
        val activity = home() ?: return
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

        votesButton.background = ContextCompat.getDrawable(activity, Vote.getDrawableColor10(votelist?.vote))

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
        loading ?: return
        bottomSheetButtons.forEach { it.isEnabled = loading <= 0 }
    }

    private fun onInitError(message: String?) {
        message ?: return
        home()?.viewModel?.onError(Throwable(message))
        findNavController().popBackStack()
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
        val activity = activity ?: return
        val intent = Intent(activity, SlideshowActivity::class.java)
        intent.putExtra(SlideshowActivity.INDEX_ARG, position)
        intent.putExtra(SlideshowActivity.IMAGES_ARG, images as Serializable)
        startActivity(intent)
    }

    override fun onTabReselected(tab: TabLayout.Tab) {
    }

    override fun onTabUnselected(tab: TabLayout.Tab) {
    }

    override fun onTabSelected(tab: TabLayout.Tab) {
        viewModel.currentPage = tab.position
    }

    override fun onDestroyView() {
        tabLayout?.removeOnTabSelectedListener(this)
        super.onDestroyView()
    }

    companion object {
        const val EXTRA_VN_ID = "vnId"
    }
}