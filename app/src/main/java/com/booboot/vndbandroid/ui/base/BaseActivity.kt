package com.booboot.vndbandroid.ui.base

import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.progress_bar.*

abstract class BaseActivity : AppCompatActivity() {
    fun showError(message: String?) {
        if (message == null) return
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show()
    }

    fun showLoading(show: Boolean?) {
        if (show == null) return
        progressBar?.visibility = if (show) View.VISIBLE else View.GONE
    }
}