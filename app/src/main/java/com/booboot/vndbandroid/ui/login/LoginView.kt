package com.booboot.vndbandroid.ui.login

interface LoginView {
    fun showError(message: String?)
    fun showResult(result: MutableSet<Int>)
    fun showLoading(show: Boolean)
}