package com.booboot.vndbandroid.ui.login

import android.os.Bundle
import android.text.method.LinkMovementMethod
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.extensions.format
import com.booboot.vndbandroid.extensions.hideKeyboard
import com.booboot.vndbandroid.extensions.observeOnce
import com.booboot.vndbandroid.extensions.startActivity
import com.booboot.vndbandroid.model.vndb.Links
import com.booboot.vndbandroid.model.vndbandroid.Preferences
import com.booboot.vndbandroid.model.vndbandroid.SyncData
import com.booboot.vndbandroid.ui.base.BaseActivity
import com.booboot.vndbandroid.ui.home.HomeActivity
import kotlinx.android.synthetic.main.login_activity.*

class LoginActivity : BaseActivity() {
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        val signupText = signupTextView.text.toString().replace("Sign up here", "[url=" + Links.VNDB_REGISTER + "]Sign up here[/url]", true)
        signupTextView.movementMethod = LinkMovementMethod.getInstance()
        signupTextView.text = signupText.format(packageName)

        loginUsername.setText(Preferences.username)
        loginPassword.setText(Preferences.password)

        loginButton.setOnClickListener {
            enableButtons(false)
            it.hideKeyboard()
            Preferences.username = loginUsername.text.toString()
            Preferences.password = loginPassword.text.toString()
            viewModel.login()
        }

        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        viewModel.loadingData.observe(this, Observer { showLoading(it) })
        viewModel.syncData.observeOnce(this, ::showResult)
        viewModel.errorData.observeOnce(this, ::showError)
    }

    private fun enableButtons(enabled: Boolean) {
        listOf(loginUsername, loginPassword, loginButton).forEach {
            it.isEnabled = enabled
        }
    }

    override fun showLoading(loading: Int?) {
        super.showLoading(loading)
        if (loading == null) return
        enableButtons(loading <= 0)
    }

    private fun showResult(result: SyncData?) {
        result ?: return
        startActivity<HomeActivity> {
            putExtra(HomeActivity.SHOULD_SYNC, false)
        }
        finish()
    }
}