package com.booboot.vndbandroid.ui.search

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.booboot.vndbandroid.App
import com.booboot.vndbandroid.model.vndb.AccountItems
import com.booboot.vndbandroid.repository.AccountRepository
import com.booboot.vndbandroid.repository.VNRepository
import com.booboot.vndbandroid.ui.base.BaseViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import javax.inject.Inject

class SearchViewModel constructor(application: Application) : BaseViewModel(application) {
    @Inject lateinit var accountRepository: AccountRepository
    @Inject lateinit var vnRepository: VNRepository

    val searchData: MutableLiveData<AccountItems> = MutableLiveData()

    var currentPage = -1
    var bottomSheetState = BottomSheetBehavior.STATE_HIDDEN

    init {
        (application as App).appComponent.inject(this)
    }

    fun getTabTitles(query: String) = coroutine(JOB_SEARCH) {
        /* Getting account items so we can show whether the results are in the user's lists */
        val items = accountRepository.getItems(this).await()
        // TODO vnRepository.search()
    }

    override fun restoreState(state: Bundle?) {
        super.restoreState(state)
        state ?: return
        currentPage = state.getInt(CURRENT_PAGE)
        bottomSheetState = state.getInt(BOTTOM_SHEET_STATE)
    }

    override fun saveState(state: Bundle) {
        super.saveState(state)
        state.putInt(CURRENT_PAGE, currentPage)
        state.putInt(BOTTOM_SHEET_STATE, bottomSheetState)
    }

    companion object {
        private const val JOB_SEARCH = "JOB_SEARCH"
        private const val CURRENT_PAGE = "CURRENT_PAGE"
        private const val BOTTOM_SHEET_STATE = "BOTTOM_SHEET_STATE"
    }
}