package com.booboot.vndbandroid.ui.restaurantdetail

interface MainView {
    fun showError(message: String?)
    fun showLoading(show: Boolean)
}