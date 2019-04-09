package com.booboot.vndbandroid.ui.base

import android.os.Bundle
import android.view.View
import com.booboot.vndbandroid.App
import com.booboot.vndbandroid.R

class DummyFragment : BaseFragment<BaseViewModel>() {
    override val layout = R.layout.empty

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = object : BaseViewModel(App.context) {}
    }
}