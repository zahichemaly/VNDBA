package com.booboot.vndbandroid.ui.login

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.model.vndb.Links
import com.booboot.vndbandroid.model.vndb.Results
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.model.vndbandroid.Preferences
import com.booboot.vndbandroid.util.Logger
import kotlinx.android.synthetic.main.login_activity.*
import kotlinx.android.synthetic.main.progress_bar.*

class LoginActivity : AppCompatActivity() {
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
        if (result == null) return
        Logger.log(result.toString())
    }

    private fun showError(message: String?) {
        if (message == null) return
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun showLoading(show: Boolean?) {
        if (show == null) return
        progressBar.visibility = if (show) VISIBLE else GONE
    }
}