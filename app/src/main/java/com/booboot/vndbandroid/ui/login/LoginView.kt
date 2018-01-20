package com.booboot.vndbandroid.ui.login

import com.booboot.vndbandroid.model.vndb.Results
import com.booboot.vndbandroid.model.vndb.VN

interface LoginView {
    fun showError(message: String?)
    fun showResult(result: Results<VN>)
    fun showLoading(show: Boolean)
}