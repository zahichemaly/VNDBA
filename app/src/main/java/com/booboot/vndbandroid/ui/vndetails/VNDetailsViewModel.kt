package com.booboot.vndbandroid.ui.vndetails

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.booboot.vndbandroid.App
import com.booboot.vndbandroid.extensions.minus
import com.booboot.vndbandroid.extensions.plus
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
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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
    var currentPage = -1

    init {
        (application as App).appComponent.inject(this)
    }

    fun loadVn(vnId: Long, force: Boolean = true) {
        if (!force && vnData.value != null) return
        if (disposables.contains(DISPOSABLE_VN)) return

        disposables[DISPOSABLE_VN] = vnRepository.getItem(vnId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingData.plus() }
            .doFinally {
                loadingData.minus()
                disposables.remove(DISPOSABLE_VN)
            }
            .subscribe({ vnData.value = it }, { onError(it, initErrorData) })
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

    fun setPriority(priority: Int) {
        val vnId = vnData.value?.id ?: return
        val wishlist = accountData.value?.wishlist?.get(vnId)
        setWishlist(wishlist?.copy(priority = priority) ?: Wishlist(vn = vnId, priority = priority))
    }

    fun removeVnlist() {
        val vnId = vnData.value?.id ?: return
        val vnlist = accountData.value?.vnlist?.get(vnId) ?: return
        vnlistRepository.deleteItem(vnlist).setAndSubscribe()
    }

    fun removeVotelist() {
        val vnId = vnData.value?.id ?: return
        val votelist = accountData.value?.votelist?.get(vnId) ?: return
        votelistRepository.deleteItem(votelist).setAndSubscribe()
    }

    fun removeWishlist() {
        val vnId = vnData.value?.id ?: return
        val wishlist = accountData.value?.wishlist?.get(vnId) ?: return
        wishlistRepository.deleteItem(wishlist).setAndSubscribe()
    }

    private fun setVnlist(vnlist: Vnlist) {
        if (accountData.value?.vnlist?.get(vnData.value?.id) == vnlist) return
        vnlistRepository.setItem(vnlist).setAndSubscribe()
    }

    private fun setVotelist(votelist: Votelist) {
        if (accountData.value?.votelist?.get(vnData.value?.id) == votelist) {
            /* In case the user inputs "8.0" as a vote, still refreshes the UI so the custom vote EditText is cleared */
            accountData.value = accountData.value
        } else {
            votelistRepository.setItem(votelist).setAndSubscribe()
        }
    }

    private fun setWishlist(wishlist: Wishlist) {
        if (accountData.value?.wishlist?.get(vnData.value?.id) == wishlist) return
        wishlistRepository.setItem(wishlist).setAndSubscribe()
    }

    private fun Completable.setAndSubscribe() = subscribeOn(Schedulers.io())
        .doOnSubscribe { loadingData.plus() }
        .observeOn(Schedulers.io())
        .andThen(accountRepository.getItems())
        .observeOn(AndroidSchedulers.mainThread())
        .doFinally { loadingData.minus() }
        .subscribe({ accountData.value = it }, ::onError)

    override fun restoreState(state: Bundle?) {
        super.restoreState(state)
        state ?: return
        currentPage = state.getInt(CURRENT_PAGE)
    }

    override fun saveState(state: Bundle) {
        super.saveState(state)
        state.putInt(CURRENT_PAGE, currentPage)
    }

    companion object {
        private const val DISPOSABLE_VN = "DISPOSABLE_VN"
        private const val CURRENT_PAGE = "CURRENT_PAGE"
    }
}