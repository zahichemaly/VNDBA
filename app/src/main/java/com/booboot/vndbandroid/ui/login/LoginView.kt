package com.booboot.vndbandroid.ui.login

interface LoginView {
    fun showError(message: String?)
    fun showLoading(show: Boolean)
}