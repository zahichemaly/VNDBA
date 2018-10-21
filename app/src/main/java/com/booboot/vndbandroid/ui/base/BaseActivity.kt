package com.booboot.vndbandroid.ui.base

import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.booboot.vndbandroid.extensions.reset
import com.booboot.vndbandroid.model.vndbandroid.EXTRA_ERROR_MESSAGE
import com.booboot.vndbandroid.model.vndbandroid.RESULT_ERROR
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.progress_bar.*

abstract class BaseActivity : AppCompatActivity() {
    open fun showError(message: String?, liveData: MutableLiveData<String>? = null) {
        message ?: return
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show()
        liveData?.reset()
    }

    open fun showLoading(loading: Int?) {
        loading ?: return
        progressBar?.visibility = if (loading > 0) View.VISIBLE else View.GONE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (resultCode) {
            RESULT_ERROR -> showError(data?.getStringExtra(EXTRA_ERROR_MESSAGE))
        }
    }
}