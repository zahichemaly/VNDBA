package com.booboot.vndbandroid.ui.base

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    lateinit var rootView: View
    lateinit var viewModel: T

    /* State saving #1 : when swiping through a ViewPager back and forth (ViewModel destroyed, native state called too early) */
    var layoutState: Parcelable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
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

    protected fun onAdapterUpdate(empty: Boolean) {
        backgroundInfo?.toggle(empty)
    }

    fun vnDetailsFragment() = parentFragment as? VNDetailsFragment

    open fun scrollToTop() {}

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.saveState(outState)
    }

    fun finish() {
        home()?.onSupportNavigateUp()
    }
}