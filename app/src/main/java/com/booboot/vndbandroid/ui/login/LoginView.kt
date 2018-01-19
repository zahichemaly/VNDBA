package com.booboot.vndbandroid.ui.login

interface LoginView {
    fun showError(message: String?)
    fun showResult(result: Set<Int>)
    fun showLoading(show: Boolean)
}