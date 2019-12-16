package com.booboot.vndbandroid.ui.login

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.extensions.hideCustomError
import com.booboot.vndbandroid.extensions.hideKeyboard
import com.booboot.vndbandroid.extensions.observeNonNull
import com.booboot.vndbandroid.extensions.openURL
import com.booboot.vndbandroid.extensions.setLightStatusAndNavigation
import com.booboot.vndbandroid.extensions.showCustomError
import com.booboot.vndbandroid.extensions.startActivity
import com.booboot.vndbandroid.extensions.toggle
import com.booboot.vndbandroid.model.vndb.Links
import com.booboot.vndbandroid.model.vndbandroid.NO
import com.booboot.vndbandroid.model.vndbandroid.NOT_SET
import com.booboot.vndbandroid.model.vndbandroid.Preferences
import com.booboot.vndbandroid.model.vndbandroid.YES
import com.booboot.vndbandroid.ui.base.BaseActivity
import com.booboot.vndbandroid.ui.home.HomeActivity
import kotlinx.android.synthetic.main.login_activity.*
import kotlinx.android.synthetic.main.login_consent.*
import kotlinx.android.synthetic.main.login_signin.*

class LoginActivity : BaseActivity() {
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        setLightStatusAndNavigation()

        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        viewModel.loadingData.observeNonNull(this, ::showLoading)
        viewModel.syncData.observeNonNull(this) { goToHome() }
        viewModel.errorData.observe(this, Observer { toggleError(it) })

        layoutConsent.isVisible = Preferences.gdprCrashlytics == NOT_SET
        layoutSignin.isVisible = Preferences.gdprCrashlytics != NOT_SET

        loginUsername.setText(Preferences.username)
        loginPassword.setText(Preferences.password)

        buttonNo.setOnClickListener {
            Preferences.gdprCrashlytics = NO
            layoutConsent.isVisible = false
            layoutSignin.isVisible = true
        }
        buttonYes.setOnClickListener {
            Preferences.gdprCrashlytics = YES
            layoutConsent.isVisible = false
            layoutSignin.isVisible = true
        }
        buttonMoreInfo.setOnClickListener { openURL(Links.PRIVACY_POLICY) }

        loginButton.setOnClickListener {
            it.hideKeyboard()
            Preferences.username = loginUsername.text.toString()
            Preferences.password = loginPassword.text.toString()
            viewModel.login()
        }
        signupButton.setOnClickListener { openURL(Links.VNDB_REGISTER) }
    }

    private fun enableButtons(enabled: Boolean) {
        listOfNotNull<View>(loginUsername, loginPassword, loginButton).forEach {
            it.isEnabled = enabled
        }
    }

    override fun showLoading(loading: Int) {
        super.showLoading(loading)
        enableButtons(loading <= 0)
        textLoading.toggle(loading > 0)
        if (loading > 0) viewModel.errorData.value = null
    }

    private fun toggleError(message: String?) {
        if (message == null) {
            loginPasswordLayout.hideCustomError(passwordError)
        } else {
            loginPasswordLayout.showCustomError(message, passwordError)
        }
    }

    private fun goToHome() {
        viewModel.errorData.value = null
        startActivity<HomeActivity> {
            putExtra(HomeActivity.SHOULD_SYNC, false)
        }
        finish()
    }
}