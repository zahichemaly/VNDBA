package com.booboot.vndbandroid.ui.vnsummary

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.ui.base.BaseFragment
import com.squareup.picasso.Picasso

class SummaryFragment : BaseFragment() {
    override val layout: Int = R.layout.summary_fragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            activity?.startPostponedEnterTransition()
//        }
    }
}