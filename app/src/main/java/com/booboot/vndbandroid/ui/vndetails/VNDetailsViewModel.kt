package com.booboot.vndbandroid.ui.vndetails

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.booboot.vndbandroid.App
import com.booboot.vndbandroid.extensions.addOrCreate
import com.booboot.vndbandroid.extensions.categorizeLabels
import com.booboot.vndbandroid.extensions.plusAssign
import com.booboot.vndbandroid.model.vndb.Label
import com.booboot.vndbandroid.model.vndb.UserLabel
import com.booboot.vndbandroid.model.vndb.UserList
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.model.vndbandroid.AccountItems
import com.booboot.vndbandroid.model.vndbandroid.Vote
import com.booboot.vndbandroid.repository.AccountRepository
import com.booboot.vndbandroid.repository.UserListRepository
import com.booboot.vndbandroid.repository.VNRepository
import com.booboot.vndbandroid.ui.base.BaseViewModel
import com.booboot.vndbandroid.ui.filters.FilterSubtitleItem
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.coroutines.CoroutineExceptionHandler
import javax.inject.Inject
import kotlin.math.roundToInt

class VNDetailsViewModel constructor(application: Application) : BaseViewModel(application) {
    @Inject lateinit var vnRepository: VNRepository
    @Inject lateinit var accountRepository: AccountRepository
    @Inject lateinit var userListRepository: UserListRepository

    val vnData = MutableLiveData<VN>()
    val userlistData = MutableLiveData<UserListData>()
    val initErrorData = MutableLiveData<String>()
    lateinit var accountData: MutableLiveData<AccountItems>

    private val initErrorHandler = CoroutineExceptionHandler { _, throwable ->
        onError(throwable, initErrorData)
    }

    var vnId: Long = 0
    var currentPage = -1
    var slideshowPosition = 0

    init {
        (application as App).appComponent.inject(this)
    }

    fun loadVn(force: Boolean = true) = coroutine(DISPOSABLE_VN, !force && vnData.value != null, initErrorHandler) {
        vnData += vnRepository.getItem(vnId)
    }

    fun getUserList(accountItems: AccountItems) = coroutine(JOB_GET_USER_LIST) {
        val userList = accountItems.userList[vnId]
        val labelIds = userList?.labelIds() ?: setOf()
        val categorizedLabelsJob = categorizeLabels(
            accountItems,
            ::onLabelClicked,
            ::onVoteClicked,
            { it in labelIds },
            { Label.voteIdToVoteOutOf100(it) == userList?.vote },
            { categorizedLabels, group, _ ->
                val customVote = if (userList?.vote?.rem(10) == 0) null else Vote.toShortString(userList?.vote, null)
                categorizedLabels.addOrCreate(group, CustomVoteItem(customVote, ::setCustomVote))
            }
        )
        userlistData += UserListData(userList, categorizedLabelsJob.await())
    }

    fun setNotes(notes: String) {
        val userList = accountData.value?.userList?.get(vnId)
        val newUserList = userList?.copy(notes = notes) ?: UserList(vn = vnId, notes = notes)
        setUserList(newUserList, mapOf("notes" to newUserList.notes))
    }

    private fun onLabelClicked(userLabel: UserLabel) {
        val userList = accountData.value?.userList?.get(vnId)
        val label = userLabel.toLabel()
        val labels = when {
            userList?.labels == null -> setOf(label)
            label.id in userList.labelIds() -> userList.labels.filter { label.id != it.id }.toSet() // Remove if already present
            else -> userList.labels - userList.labels(label.getGroup()) + label // Remove other labels in the same group before adding it
        }

        val newUserList = userList?.copy(labels = labels) ?: UserList(vn = vnId, labels = labels)
        setUserList(newUserList, mapOf("labels" to newUserList.labelIds()))
    }

    private fun onVoteClicked(label: UserLabel) = setVote(Label.voteIdToVoteOutOf100(label.id))

    private fun setVote(_vote: Int, customVote: Boolean = false) {
        val userList = accountData.value?.userList?.get(vnId)
        val vote = if (!customVote && _vote == userList?.vote) {
            /* Remove if already present ; Add otherwise */
            null
        } else _vote

        if (userList?.vote == vote) {
            /* In case the user inputs "8.0" as a vote, still refreshes the UI so the custom vote EditText is cleared */
            accountData += accountData.value
        } else {
            val newUserList = userList?.copy(vote = vote) ?: UserList(vn = vnId, vote = vote)
            setUserList(newUserList, mapOf("vote" to newUserList.vote))
        }
    }

    private fun setCustomVote(voteText: String?) {
        if (voteText?.isEmpty() != false) return

        val vote = voteText.toFloatOrNull()?.let {
            (it * 10).roundToInt()
        } ?: return onError(Throwable("The vote must be a number with one decimal digit (e.g. 8.3)."))

        if (vote < 10 || vote > 100) return onError(Throwable("The vote must be between 1 and 10."))

        setVote(vote, true)
    }

    private fun setUserList(userList: UserList, changesMap: Map<String, Any?>) = coroutine(JOB_SET_VNLIST, accountData.value?.userList?.get(vnData.value?.id) == userList) {
        userListRepository.setItem(userList, changesMap)
        accountData += accountRepository.getItems()
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
        private const val JOB_SET_VNLIST = "JOB_SET_VNLIST"
        private const val JOB_GET_USER_LIST = "JOB_GET_USER_LIST"
    }
}

data class UserListData(
    val userList: UserList?,
    val categorizedLabels: MutableMap<FilterSubtitleItem, MutableList<Item>>
)