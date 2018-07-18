package com.booboot.vndbandroid.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.booboot.vndbandroid.ui.home.HomeActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.progress_bar.*
import kotlinx.android.synthetic.main.vn_list_fragment.*

/**
 * Created by od on 13/03/2016.
 */
abstract class BaseFragment : Fragment() {
    abstract val layout: Int
    lateinit var rootView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(layout, container, false)
        return rootView
    }

    fun showError(message: String?) {
        if (message == null) return
        val view = activity?.findViewById<View>(android.R.id.content) ?: return
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }

    open fun showLoading(show: Boolean?) {
        if (show == null) return
        progressBar?.visibility = if (show) View.VISIBLE else View.GONE
        refreshLayout.isRefreshing = show
    }

    fun home() = (activity as? HomeActivity?)
}