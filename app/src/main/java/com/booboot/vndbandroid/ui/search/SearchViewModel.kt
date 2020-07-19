package com.booboot.vndbandroid.ui.search

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import com.booboot.vndbandroid.App
import com.booboot.vndbandroid.diff.VNDiffCallback
import com.booboot.vndbandroid.extensions.plusAssign
import com.booboot.vndbandroid.model.vndbandroid.AccountItems
import com.booboot.vndbandroid.model.vndbandroid.VnlistData
import com.booboot.vndbandroid.repository.AccountRepository
import com.booboot.vndbandroid.repository.VNRepository
import com.booboot.vndbandroid.ui.base.BaseViewModel
import kotlinx.coroutines.async
import javax.inject.Inject

class SearchViewModel constructor(application: Application) : BaseViewModel(application) {
    @Inject lateinit var accountRepository: AccountRepository
    @Inject lateinit var vnRepository: VNRepository

    val searchData = MutableLiveData<VnlistData>()

    var currentPage = 1

    init {
        (application as App).appComponent.inject(this)
    }

    fun search(query: String) = coroutine(JOB_SEARCH, skip = query.isEmpty()) {
        /* Getting account items so we can show whether the results are in the user's lists */
        val accountItemsJob = async { accountRepository.getItems() }
        val vnJob = async { vnRepository.search(currentPage, query) }
        val accountItems = accountItemsJob.await().apply {
            vns = vnJob.await()
        }
        val diffResult = DiffUtil.calculateDiff(VNDiffCallback(searchData.value?.items ?: AccountItems(), accountItems))
        searchData += VnlistData(accountItems, diffResult)
    }

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
        private const val JOB_SEARCH = "JOB_SEARCH"
        private const val CURRENT_PAGE = "CURRENT_PAGE"
    }
}