package com.booboot.vndbandroid.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.transition.TransitionInflater
import com.booboot.vndbandroid.extensions.home
import com.booboot.vndbandroid.extensions.startParentEnterTransition
import com.booboot.vndbandroid.extensions.toggle
import com.booboot.vndbandroid.ui.vndetails.VNDetailsFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.progress_bar.*
import kotlinx.android.synthetic.main.vn_list_fragment.*

abstract class BaseFragment<T : BaseViewModel> : Fragment() {
    abstract val layout: Int
    open val softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
    lateinit var rootView: View
    lateinit var viewModel: T

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (parentFragment !is BaseFragment<*>) {
            activity?.window?.setSoftInputMode(softInputMode)
        }

        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        sharedElementReturnTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)

        rootView = inflater.inflate(layout, container, false)
        return rootView
    }

    fun onError() {
        startPostponedEnterTransition()
        startParentEnterTransition()
    }

    open fun showError(message: String) {
        onError()
        val view = activity?.findViewById<View>(android.R.id.content) ?: return
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }

    open fun showLoading(loading: Int?) {
        loading ?: return
        progressBar?.visibility = if (loading > 0) View.VISIBLE else View.GONE
        refreshLayout?.isRefreshing = loading > 0
    }

    @CallSuper
    protected open fun onAdapterUpdate(empty: Boolean) {
        backgroundInfo?.toggle(empty)
    }

    fun vnDetailsFragment() = parentFragment as? VNDetailsFragment

    open fun scrollToTop() {}

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (::viewModel.isInitialized) {
            viewModel.saveState(outState)
        }
    }

    fun finish() {
        home()?.onSupportNavigateUp()
    }
}