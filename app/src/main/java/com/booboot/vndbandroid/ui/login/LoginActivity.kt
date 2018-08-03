package com.booboot.vndbandroid.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.model.vndb.Links
import com.booboot.vndbandroid.model.vndbandroid.Preferences
import com.booboot.vndbandroid.model.vndbandroid.SyncData
import com.booboot.vndbandroid.ui.base.BaseActivity
import com.booboot.vndbandroid.ui.home.HomeActivity
import com.booboot.vndbandroid.util.Logger
import kotlinx.android.synthetic.main.login_activity.*

class LoginActivity : BaseActivity() {
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        Links.setTextViewLink(this, signupTextView, Links.VNDB_REGISTER, signupTextView.text.toString().indexOf("Sign up here"), signupTextView.text.toString().length)

        loginUsername.setText(Preferences.username)
        loginPassword.setText(Preferences.password)

        loginButton.setOnClickListener {
            Preferences.username = loginUsername.text.toString()
            Preferences.password = loginPassword.text.toString()
            viewModel.login()
        }

        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        viewModel.loadingData.observe(this, Observer { showLoading(it) })
        viewModel.syncData.observe(this, Observer { showResult(it) })
        viewModel.errorData.observe(this, Observer { showError(it) })
    }

    private fun showResult(result: SyncData?) {
        if (result == null) return
        startActivity(Intent(this, HomeActivity::class.java))
    }
}