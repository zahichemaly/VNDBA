package com.booboot.vndbandroid.ui.login

import com.booboot.vndbandroid.model.vndb.Results
import com.booboot.vndbandroid.model.vndbandroid.VNlistItem

interface LoginView {
    fun showError(message: String?)
    fun showResult(result: Results<VNlistItem>)
    fun showLoading(show: Boolean)
}