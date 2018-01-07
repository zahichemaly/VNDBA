package com.booboot.vndbandroid.ui.login

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import com.booboot.vndbandroid.App
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.model.api.Links
import com.booboot.vndbandroid.util.PreferencesManager
import kotlinx.android.synthetic.main.login_activity.*
import kotlinx.android.synthetic.main.progress_bar.*
import javax.inject.Inject

class LoginActivity : AppCompatActivity(), LoginView {
    @Inject
    lateinit var presenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        (application as App).appComponent.inject(this)

        Links.setTextViewLink(this, signupTextView, Links.VNDB_REGISTER, signupTextView.text.toString().indexOf("Sign up here"), signupTextView.text.toString().length)

        loginUsername.setText(PreferencesManager.username())
        loginPassword.setText(PreferencesManager.password())
        loginButton.setOnClickListener { presenter.login() }

        presenter.attachView(this)
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    override fun showError(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) VISIBLE else GONE
    }
}