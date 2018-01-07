package com.booboot.vndbandroid.ui.home

interface MainView {
    fun showError(message: String?)
    fun showLoading(show: Boolean)
}