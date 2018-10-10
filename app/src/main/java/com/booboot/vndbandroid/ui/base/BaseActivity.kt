package com.booboot.vndbandroid.ui.base

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.progress_bar.*

abstract class BaseActivity : AppCompatActivity() {
    open fun showError(message: String?) {
        if (message == null) return
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show()
    }

    open fun showLoading(loading: Int?) {
        if (loading == null) return
        progressBar?.visibility = if (loading > 0) View.VISIBLE else View.GONE
    }
}