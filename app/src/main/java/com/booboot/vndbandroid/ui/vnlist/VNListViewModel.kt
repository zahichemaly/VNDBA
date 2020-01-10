package com.booboot.vndbandroid.ui.vnlist

import android.app.Application
import com.booboot.vndbandroid.model.vndbandroid.AccountItems
import com.booboot.vndbandroid.ui.base.BaseViewModel

class VNListViewModel constructor(application: Application) : BaseViewModel(application) {
    var filteredVns: AccountItems? = null
}