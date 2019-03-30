package com.booboot.vndbandroid.extensions

import android.view.View
import androidx.core.view.ViewCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.booboot.vndbandroid.NavigationDirections
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.ui.base.BaseViewModel

fun NavController.openVN(vn: VN, transitionView: View, viewModel: BaseViewModel) {
    viewModel.hasPendingTransition = true
    navigate(
        NavigationDirections.openVN(vn.id, vn.image, vn.image_nsfw),
        FragmentNavigatorExtras(transitionView to (ViewCompat.getTransitionName(transitionView) ?: ""))
    )
}