package com.booboot.vndbandroid.extensions

import android.view.View
import androidx.core.view.ViewCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.booboot.vndbandroid.NavigationDirections
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.model.vndbandroid.Preferences
import com.booboot.vndbandroid.ui.base.BaseViewModel

fun NavController.openVN(vn: VN, transitionView: View, viewModel: BaseViewModel) {
    viewModel.hasPendingTransition = true
    val extras = if (Preferences.useSharedTransitions) {
        FragmentNavigatorExtras(transitionView to (ViewCompat.getTransitionName(transitionView) ?: ""))
    } else {
        FragmentNavigatorExtras()
    }
    navigate(
        NavigationDirections.openVN(vn.id, vn.image, vn.image_nsfw),
        extras
    )
}

fun NavController.openSlideshow(vnId: Long, position: Int = 0) = navigate(
    NavigationDirections.openSlideshow(vnId, position)
)