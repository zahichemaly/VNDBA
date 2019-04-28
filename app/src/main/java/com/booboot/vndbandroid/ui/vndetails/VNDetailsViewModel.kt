package com.booboot.vndbandroid.ui.vndetails

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.booboot.vndbandroid.App
import com.booboot.vndbandroid.model.vndb.AccountItems
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.model.vndb.Vnlist
import com.booboot.vndbandroid.model.vndb.Votelist
import com.booboot.vndbandroid.model.vndb.Wishlist
import com.booboot.vndbandroid.repository.AccountRepository
import com.booboot.vndbandroid.repository.VNRepository
import com.booboot.vndbandroid.repository.VnlistRepository
import com.booboot.vndbandroid.repository.VotelistRepository
import com.booboot.vndbandroid.repository.WishlistRepository
import com.booboot.vndbandroid.ui.base.BaseViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import javax.inject.Inject

class VNDetailsViewModel constructor(application: Application) : BaseViewModel(application) {
    @Inject lateinit var vnRepository: VNRepository
    @Inject lateinit var accountRepository: AccountRepository
    @Inject lateinit var vnlistRepository: VnlistRepository
    @Inject lateinit var votelistRepository: VotelistRepository
    @Inject lateinit var wishlistRepository: WishlistRepository

    val vnData: MutableLiveData<VN> = MutableLiveData()
    val initErrorData: MutableLiveData<String> = MutableLiveData()
    lateinit var accountData: MutableLiveData<AccountItems>

    private val initErrorHandler = CoroutineExceptionHandler { _, throwable ->
        onError(throwable, initErrorData)
    }

    var currentPage = -1
    var slideshowPosition = 0

    init {
        (application as App).appComponent.inject(this)
    }

    fun loadVn(vnId: Long, force: Boolean = true) = coroutine(DISPOSABLE_VN, !force && vnData.value != null, initErrorHandler) {
        vnData.value = vnRepository.getItem(this, vnId).await()
    }

    fun setNotes(notes: String) {
        val vnId = vnData.value?.id ?: return
        val vnlist = accountData.value?.vnlist?.get(vnId)
        setVnlist(vnlist?.copy(notes = notes) ?: Vnlist(vn = vnId, notes = notes))
    }

    fun setStatus(status: Int) {
        val vnId = vnData.value?.id ?: return
        val vnlist = accountData.value?.vnlist?.get(vnId)
        setVnlist(vnlist?.copy(status = status) ?: Vnlist(vn = vnId, status = status))
    }

    fun setVote(vote: Int) {
        val vnId = vnData.value?.id ?: return
        val votelist = accountData.value?.votelist?.get(vnId)
        setVotelist(votelist?.copy(vote = vote) ?: Votelist(vn = vnId, vote = vote))
    }

    fun setCustomVote(voteText: String?) {
        if (voteText?.isEmpty() != false) return

        val vote = voteText.toFloatOrNull()?.let {
            Math.round(it * 10)
        } ?: return onError(Throwable("The vote must be a number with one decimal digit (e.g. 8.3)."))

        if (vote < 10 || vote > 100) return onError(Throwable("The vote must be between 1 and 10."))

        setVote(vote)
    }

    fun setPriority(priority: Int) = coroutine(JOB_SET_PRIORITY) {
        val vnId = vnData.value?.id ?: return@coroutine
        val wishlist = accountData.value?.wishlist?.get(vnId)
        setWishlist(wishlist?.copy(priority = priority) ?: Wishlist(vn = vnId, priority = priority))
    }

    fun removeVnlist() = coroutine(JOB_REMOVE_VNLIST) {
        val vnId = vnData.value?.id ?: return@coroutine
        val vnlist = accountData.value?.vnlist?.get(vnId) ?: return@coroutine
        vnlistRepository.deleteItem(this, vnlist).setAndSubscribe(this)
    }

    fun removeVotelist() = coroutine(JOB_REMOVE_VOTELIST) {
        val vnId = vnData.value?.id ?: return@coroutine
        val votelist = accountData.value?.votelist?.get(vnId) ?: return@coroutine
        votelistRepository.deleteItem(this, votelist).setAndSubscribe(this)
    }

    fun removeWishlist() = coroutine(JOB_REMOVE_WISHLIST) {
        val vnId = vnData.value?.id ?: return@coroutine
        val wishlist = accountData.value?.wishlist?.get(vnId) ?: return@coroutine
        wishlistRepository.deleteItem(this, wishlist).setAndSubscribe(this)
    }

    private fun setVnlist(vnlist: Vnlist) = coroutine(JOB_SET_VNLIST, accountData.value?.vnlist?.get(vnData.value?.id) == vnlist) {
        vnlistRepository.setItem(this, vnlist).setAndSubscribe(this)
    }

    private fun setVotelist(votelist: Votelist) = coroutine(JOB_SET_VOTELIST) {
        if (accountData.value?.votelist?.get(vnData.value?.id) == votelist) {
            /* In case the user inputs "8.0" as a vote, still refreshes the UI so the custom vote EditText is cleared */
            accountData.value = accountData.value
        } else {
            votelistRepository.setItem(this, votelist).setAndSubscribe(this)
        }
    }

    private fun setWishlist(wishlist: Wishlist) = coroutine(JOB_SET_WISHLIST, accountData.value?.wishlist?.get(vnData.value?.id) == wishlist) {
        wishlistRepository.setItem(this, wishlist).setAndSubscribe(this)
    }

    private suspend fun Deferred<*>.setAndSubscribe(coroutineScope: CoroutineScope) {
        await()
        accountData.value = accountRepository.getItems(coroutineScope).await()
    }

    override fun restoreState(state: Bundle?) {
        super.restoreState(state)
        state ?: return
        currentPage = state.getInt(CURRENT_PAGE)
        slideshowPosition = state.getInt(SLIDESHOW_POSITION)
    }

    override fun saveState(state: Bundle) {
        super.saveState(state)
        state.putInt(CURRENT_PAGE, currentPage)
        state.putInt(SLIDESHOW_POSITION, slideshowPosition)
    }

    companion object {
        private const val DISPOSABLE_VN = "DISPOSABLE_VN"
        private const val CURRENT_PAGE = "CURRENT_PAGE"
        private const val SLIDESHOW_POSITION = "SLIDESHOW_POSITION"
        private const val JOB_SET_WISHLIST = "JOB_SET_WISHLIST"
        private const val JOB_SET_VOTELIST = "JOB_SET_VOTELIST"
        private const val JOB_SET_VNLIST = "JOB_SET_VNLIST"
        private const val JOB_REMOVE_WISHLIST = "JOB_REMOVE_WISHLIST"
        private const val JOB_REMOVE_VOTELIST = "JOB_REMOVE_VOTELIST"
        private const val JOB_REMOVE_VNLIST = "JOB_REMOVE_VNLIST"
        private const val JOB_SET_PRIORITY = "JOB_SET_PRIORITY"
    }
}