package com.booboot.vndbandroid.ui.login

import com.booboot.vndbandroid.model.vndbandroid.AccountItems

interface LoginView {
    fun showError(message: String?)
    fun showResult(result: AccountItems)
    fun showLoading(show: Boolean)
}