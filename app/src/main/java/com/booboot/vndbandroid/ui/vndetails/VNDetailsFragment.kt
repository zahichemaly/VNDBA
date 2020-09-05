package com.booboot.vndbandroid.ui.vndetails

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.extensions.fixForFastScroll
import com.booboot.vndbandroid.extensions.fixScrollWithBottomSheet
import com.booboot.vndbandroid.extensions.home
import com.booboot.vndbandroid.extensions.observeNonNull
import com.booboot.vndbandroid.extensions.observeOnce
import com.booboot.vndbandroid.extensions.onStateChanged
import com.booboot.vndbandroid.extensions.openSlideshow
import com.booboot.vndbandroid.extensions.postponeEnterTransitionIfExists
import com.booboot.vndbandroid.extensions.removeFocus
import com.booboot.vndbandroid.extensions.replaceOnTabSelectedListener
import com.booboot.vndbandroid.extensions.setStatusBarThemeForCollapsingToolbar
import com.booboot.vndbandroid.extensions.setupStatusBar
import com.booboot.vndbandroid.extensions.setupToolbar
import com.booboot.vndbandroid.extensions.toggle
import com.booboot.vndbandroid.extensions.toggleBottomSheet
import com.booboot.vndbandroid.model.vndb.Screen
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.model.vndbandroid.Vote
import com.booboot.vndbandroid.ui.base.BaseFragment
import com.booboot.vndbandroid.ui.slideshow.SlideshowAdapter
import com.booboot.vndbandroid.util.StopFocusStealingAppBarBehavior
import com.booboot.vndbandroid.util.view.LockableBottomSheetBehavior
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayout
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.vn_details_bottom_sheet.*
import kotlinx.android.synthetic.main.vn_details_fragment.*

class VNDetailsFragment : BaseFragment<VNDetailsViewModel>(), TabLayout.OnTabSelectedListener, ViewPager.OnPageChangeListener {
    override val layout = R.layout.vn_details_fragment

    /* Layout with CollapsingToolbarLayout doesn't work with adjustResize (Android bug : https://stackoverflow.com/a/39099510/4561039) */
    override val softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
    private lateinit var slideshowAdapter: SlideshowAdapter
    private var tabsAdapter: VNDetailsTabsAdapter? = null
    private val bottomSheetAdapter = GroupAdapter<GroupieViewHolder>()

    lateinit var bottomSheetBehavior: LockableBottomSheetBehavior<*>
//    private lateinit var bottomSheetButtons: List<View?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val activity = home() ?: return

        setupStatusBar(true)
        setupToolbar()
        appBarLayout.setStatusBarThemeForCollapsingToolbar(activity, collapsingToolbar, toolbar, content)

        viewModel = ViewModelProvider(this).get(VNDetailsViewModel::class.java)
        viewModel.restoreState(savedInstanceState)
        viewModel.accountData = home()?.viewModel?.accountData ?: return
        viewModel.loadingData.observeNonNull(this, ::showLoading)
        viewModel.vnData.observeNonNull(this, ::showVn)
        viewModel.userlistData.observeNonNull(this, ::showAccount)
        home()?.viewModel?.accountData?.observeNonNull(this) { viewModel.getUserList(it) }
        viewModel.errorData.observeOnce(this, ::showError)
        viewModel.initErrorData.observeOnce(this, ::onInitError)

        arguments?.let { arguments ->
            viewModel.vnId = VNDetailsFragmentArgs.fromBundle(arguments).vnId
            val vnImage = VNDetailsFragmentArgs.fromBundle(arguments).vnImage
            val vnImageNsfw = VNDetailsFragmentArgs.fromBundle(arguments).vnImageNsfw

            slideshowAdapter = SlideshowAdapter(layoutInflater, ::onImageClicked, scaleType = ImageView.ScaleType.CENTER_CROP)
            slideshow.adapter = slideshowAdapter
            slideshow.addOnPageChangeListener(this)

            vnImage?.let {
                slideshowAdapter.images = mutableListOf(Screen(image = vnImage, nsfw = vnImageNsfw))
            }
        }

        if (tabsAdapter == null) {
            tabsAdapter = VNDetailsTabsAdapter(childFragmentManager)
        } else {
            postponeEnterTransitionIfExists()
        }
        viewPager.adapter = tabsAdapter
        tabLayout.setupWithViewPager(viewPager)

        /* View groups */
//        bottomSheetButtons = listOf(textNotes, buttonPlaying, buttonFinished, buttonStalled, buttonDropped, buttonUnknown,
//            buttonVote1, buttonVote2, buttonVote3, buttonVote4, buttonVote5, buttonVote6, buttonVote7, buttonVote8, buttonVote9, buttonVote10, inputCustomVote,
//            buttonWishlistHigh, buttonWishlistBlacklist)

        /* Bottom sheet */
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet) as LockableBottomSheetBehavior<*>
        bottomSheetHeader.setOnClickListener { bottomSheet.toggleBottomSheet() }
        bottomSheetBehavior.onStateChanged(
            onCollapsed = {
                removeFocus()
                iconArrow.setImageResource(R.drawable.ic_keyboard_arrow_down_white_24dp)
            },
            onExpanding = { iconArrow.setImageResource(R.drawable.ic_keyboard_arrow_up_white_24dp) }
        )
        appBarLayout.fixForFastScroll(viewPager)
        appBarLayout.updateLayoutParams<CoordinatorLayout.LayoutParams> {
            behavior = StopFocusStealingAppBarBehavior(bottomSheet)
        }
//        textNotes.preventLineBreak()
//        bottomSheetButtons.forEach { it?.setOnClickListener(this@VNDetailsFragment) }
//        textNotes.onFocusChangeListener = this
//        inputCustomVote.onFocusChangeListener = this

        filters.layoutManager = FlexboxLayoutManager(activity).apply {
            alignItems = AlignItems.CENTER
            justifyContent = JustifyContent.FLEX_START
        }
        filters.adapter = bottomSheetAdapter
        filters.fixScrollWithBottomSheet(bottomSheetBehavior)

        slideshow.transitionName = "slideshow${viewModel.vnId}"

        viewModel.loadVn()
    }

    private fun showVn(vn: VN) {
        home()?.supportActionBar?.title = vn.title

        val screens = vn.image?.let { mutableListOf(Screen(image = it, nsfw = vn.image_nsfw)) } ?: mutableListOf()
        screens.addAll(vn.screens)
        slideshowAdapter.images = screens
        slideshow.setCurrentItem(viewModel.slideshowPosition, false)
        numberOfImages.text = String.format("x%d", screens.size)

        tabsAdapter?.vn = vn
        if (viewModel.currentPage >= 0) viewPager.currentItem = viewModel.currentPage
        tabLayout.replaceOnTabSelectedListener(this)
    }

    private fun showAccount(userListData: UserListData) = userListData.run {
        val activity = home() ?: return

        val firstStatus = userList?.firstStatus()
        val firstWishlist = userList?.firstWishlist()
        val vote = Vote.toShortString(userList?.vote, null)
//        val labelIds = userList?.labelIds() ?: setOf()

        textAddToList.text = when (userList) {
            null -> getString(R.string.add_to_your_lists)
            else -> getString(R.string.edit_your_lists)
        }

        textStatus.text = firstStatus?.label
        textWishlist.text = firstWishlist?.label
        votesButton.text = vote ?: getString(R.string.dash)

        iconStatus.toggle(firstStatus != null)
        textStatus.toggle(firstStatus != null)
        iconWishlist.toggle(firstWishlist != null)
        textWishlist.toggle(firstWishlist != null)

        votesButton.background = ContextCompat.getDrawable(activity, Vote.getDrawableColor10(userList?.vote))

        bottomSheetAdapter.updateAsync(
            listOf(NotesItem(userList?.notes, ::setNotes)) +
                categorizedLabels.flatMap { (subtitle, items) ->
                    listOf(subtitle) + items
                }
        )

//        textNotes.setText(userList?.notes, TextView.BufferType.EDITABLE)
//        buttonPlaying.selectIf(PLAYING.id in labelIds)
//        buttonFinished.selectIf(FINISHED.id in labelIds)
//        buttonStalled.selectIf(STALLED.id in labelIds)
//        buttonDropped.selectIf(DROPPED.id in labelIds)
//        buttonUnknown.selectIf(UNKNOWN.id in labelIds)
//        buttonVote1.selectIf(userList?.vote == 10)
//        buttonVote2.selectIf(userList?.vote == 20)
//        buttonVote3.selectIf(userList?.vote == 30)
//        buttonVote4.selectIf(userList?.vote == 40)
//        buttonVote5.selectIf(userList?.vote == 50)
//        buttonVote6.selectIf(userList?.vote == 60)
//        buttonVote7.selectIf(userList?.vote == 70)
//        buttonVote8.selectIf(userList?.vote == 80)
//        buttonVote9.selectIf(userList?.vote == 90)
//        buttonVote10.selectIf(userList?.vote == 100)
//        inputCustomVote.setText(if (userList?.vote?.rem(10) == 0) null else vote)
//        buttonWishlistHigh.selectIf(WISHLIST.id in labelIds)
//        buttonWishlistBlacklist.selectIf(BLACKLIST.id in labelIds)
    }

    override fun showLoading(loading: Int) {
        super.showLoading(loading)
//        bottomSheetButtons.forEach { it?.isEnabled = loading <= 0 }
    }

    private fun onInitError(message: String) {
        super.onError()
        home()?.viewModel?.onError(Throwable(message))
        findNavController().popBackStack()
    }

//    override fun onClick(view: View) {
//        when (view.id) {
//            R.id.bottomSheetHeader -> bottomSheet.toggleBottomSheet()

//            R.id.buttonPlaying -> viewModel.toggleLabel(PLAYING, STATUSES)
//            R.id.buttonFinished -> viewModel.toggleLabel(FINISHED, STATUSES)
//            R.id.buttonStalled -> viewModel.toggleLabel(STALLED, STATUSES)
//            R.id.buttonDropped -> viewModel.toggleLabel(DROPPED, STATUSES)
//            R.id.buttonUnknown -> viewModel.toggleLabel(UNKNOWN, STATUSES)
//
//            R.id.buttonVote1 -> viewModel.setVote(10)
//            R.id.buttonVote2 -> viewModel.setVote(20)
//            R.id.buttonVote3 -> viewModel.setVote(30)
//            R.id.buttonVote4 -> viewModel.setVote(40)
//            R.id.buttonVote5 -> viewModel.setVote(50)
//            R.id.buttonVote6 -> viewModel.setVote(60)
//            R.id.buttonVote7 -> viewModel.setVote(70)
//            R.id.buttonVote8 -> viewModel.setVote(80)
//            R.id.buttonVote9 -> viewModel.setVote(90)
//            R.id.buttonVote10 -> viewModel.setVote(100)
//
//            R.id.buttonWishlistHigh -> viewModel.toggleLabel(WISHLIST, WISHLISTS)
//            R.id.buttonWishlistBlacklist -> viewModel.toggleLabel(BLACKLIST, WISHLISTS)
//        }
//    }

    private fun setNotes(notes: String?) = viewModel.setNotes(notes ?: "")

//    override fun onFocusChange(view: View, hasFocus: Boolean) {
//        if (hasFocus) return

//        when (view.id) {
//            R.id.textNotes -> viewModel.setNotes(textNotes.text.toString())
//            R.id.inputCustomVote -> viewModel.setCustomVote(inputCustomVote.text.toString())
//        }
//    }

    private fun onImageClicked(position: Int) = findNavController().openSlideshow(viewModel.vnId, position)

    override fun onTabReselected(tab: TabLayout.Tab) {
        tabsAdapter?.getFragment(tab.position)?.scrollToTop()
    }

    override fun onTabUnselected(tab: TabLayout.Tab) {
    }

    override fun onTabSelected(tab: TabLayout.Tab) {
        viewModel.currentPage = tab.position
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        viewModel.slideshowPosition = position
    }

    override fun onDestroyView() {
        tabLayout?.removeOnTabSelectedListener(this)
        super.onDestroyView()
    }

    companion object {
        const val EXTRA_VN_ID = "vnId"
    }
}