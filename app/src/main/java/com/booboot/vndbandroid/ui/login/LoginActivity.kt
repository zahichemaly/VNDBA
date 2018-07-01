package com.booboot.vndbandroid.ui.login

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.model.vndb.Links
import com.booboot.vndbandroid.model.vndb.Results
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.model.vndbandroid.Preferences
import com.booboot.vndbandroid.ui.base.BaseActivity
import com.booboot.vndbandroid.ui.home.MainActivity
import com.booboot.vndbandroid.util.Logger
import kotlinx.android.synthetic.main.login_activity.*

class LoginActivity : BaseActivity() {
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        Links.setTextViewLink(this, signupTextView, Links.VNDB_REGISTER, signupTextView.text.toString().indexOf("Sign up here"), signupTextView.text.toString().length)

        loginUsername.setText(Preferences.username)
        loginPassword.setText(Preferences.password)

        loginButton.setOnClickListener {
            Preferences.username = loginUsername.text.toString()
            Preferences.password = loginPassword.text.toString()
            loginViewModel.login()
        }

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        loginViewModel.loadingData.observe(this, Observer { showLoading(it) })
        loginViewModel.vnData.observe(this, Observer { showResult(it) })
        loginViewModel.errorData.observe(this, Observer { showError(it) })
    }

    private fun showResult(result: Results<VN>?) {
        Logger.log(result.toString())
        startActivity(Intent(this, MainActivity::class.java))
    }
}