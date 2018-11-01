package com.booboot.vndbandroid.ui.login

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.extensions.hideKeyboard
import com.booboot.vndbandroid.extensions.openURL
import com.booboot.vndbandroid.extensions.startActivity
import com.booboot.vndbandroid.extensions.toggle
import com.booboot.vndbandroid.model.vndb.Links
import com.booboot.vndbandroid.model.vndbandroid.Preferences
import com.booboot.vndbandroid.model.vndbandroid.SyncData
import com.booboot.vndbandroid.ui.base.BaseActivity
import com.booboot.vndbandroid.ui.home.HomeActivity
import com.booboot.vndbandroid.ui.vnengine.Step
import com.booboot.vndbandroid.ui.vnengine.VnEngine
import kotlinx.android.synthetic.main.login_activity.*
import kotlinx.android.synthetic.main.login_signin.*
import kotlinx.android.synthetic.main.login_signin.view.*

class LoginActivity : BaseActivity() {
    private lateinit var viewModel: LoginViewModel
    private lateinit var vnEngine: VnEngine
    private lateinit var errorStep: Step
    private lateinit var loadingStep: Step

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        viewModel.loadingData.observe(this, Observer { showLoading(it) })
        viewModel.syncData.observe(this, Observer { showResult(it) })
        viewModel.errorData.observe(this, Observer { showError(it) })

        errorStep = Step(
            R.drawable.street001_day,
            R.drawable.yuki_sad,
            R.string.yuki_vndba_guide,
            String.format(getString(R.string.login_step_error), getString(R.string.generic_error)),
            skipOnTap = false,
            showIf = { viewModel.loginError() },
            customLayout = R.layout.login_signin,
            onShow = { setupLogin(it) }
        )

        loadingStep = Step(R.drawable.street001_day, R.drawable.yuki_dokidoki, R.string.yuki_vndba_guide, getString(R.string.login_step_9), skipOnTap = false, showIf = { viewModel.loadingData.value ?: 0 > 0 })

        vnEngine = VnEngine(layoutInflater, background, yuki, yukiName, textYuki, customLayout, listOf(
            Step(R.drawable.street001_day, R.drawable.yuki_default, R.string.yuki_vndba_guide, getString(R.string.login_step_1)),
            Step(R.drawable.street001_day, R.drawable.yuki_hesitant, R.string.yuki_vndba_guide, getString(R.string.login_step_2), skipOnTap = false, customLayout = R.layout.login_signin, onShow = { setupLogin(it) }),
            loadingStep,
            Step(R.drawable.street001_day, R.drawable.yuki_happy, R.string.yuki_vndba_guide, getString(R.string.login_step_10), showIf = { viewModel.loginOk() }, skipOnTap = false, onShow = { goToHome() }),
            errorStep
        ), ::goToHome)
        vnEngine.stepIndex = viewModel.stepIndex
        vnEngine.show()
    }

    private fun setupLogin(parent: View) = with(parent) {
        loginUsername?.setText(Preferences.username)
        loginPassword?.setText(Preferences.password)

        enableButtons(viewModel.loadingData.value ?: 0 <= 0)
        loginButton?.setOnClickListener {
            it.hideKeyboard()
            Preferences.username = loginUsername.text.toString()
            Preferences.password = loginPassword.text.toString()
            viewModel.login()

            if (vnEngine.currentStep() !== errorStep) vnEngine.nextStep()
        }
        signupButton?.setOnClickListener { openURL(Links.VNDB_REGISTER) }
    }

    private fun enableButtons(enabled: Boolean) {
        listOfNotNull(loginUsername as View?, loginPassword, loginButton).forEach {
            it.isEnabled = enabled
        }
    }

    override fun showLoading(loading: Int?) {
        super.showLoading(loading)
        loading ?: return
        enableButtons(loading <= 0)
        textLoading.toggle(loading > 0)
    }

    override fun showError(message: String?) {
        message ?: return
        errorStep.text = String.format(getString(R.string.login_step_error), message)
        when (vnEngine.currentStep()) {
            loadingStep -> vnEngine.nextStep()
            errorStep -> vnEngine.show()
        }
    }

    private fun showResult(result: SyncData?) {
        result ?: return
        when (vnEngine.currentStep()) {
            loadingStep -> vnEngine.nextStep()
            errorStep -> goToHome()
        }
    }

    private fun goToHome() {
        startActivity<HomeActivity> {
            putExtra(HomeActivity.SHOULD_SYNC, false)
        }
        finish()
    }
}