package com.booboot.vndbandroid.ui.logout

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.extensions.observeOnce
import com.booboot.vndbandroid.ui.base.BaseFragment
import com.booboot.vndbandroid.ui.login.LoginActivity

class LogoutFragment : BaseFragment<LogoutViewModel>() {
    override val layout = R.layout.logout_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this).get(LogoutViewModel::class.java)
        viewModel.logoutData.observeOnce(viewLifecycleOwner) { exit() }
        viewModel.logout()
    }

    private fun exit() {
        val activity = activity ?: return
        startActivity(Intent(activity, LoginActivity::class.java))
        activity.finish()
    }
}