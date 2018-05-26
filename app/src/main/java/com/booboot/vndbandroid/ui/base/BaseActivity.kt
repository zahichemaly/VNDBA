package com.booboot.vndbandroid.ui.base

import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

abstract class BaseActivity : AppCompatActivity() {
    fun showError(message: String?) {
        if (message == null) return
        Snackbar.make(findViewById(android.R.id.content), message, Toast.LENGTH_LONG).show()
    }
}