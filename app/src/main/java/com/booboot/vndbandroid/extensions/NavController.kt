package com.booboot.vndbandroid.extensions

import android.view.View
import androidx.core.view.ViewCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.booboot.vndbandroid.NavigationDirections
import com.booboot.vndbandroid.model.vndb.VN

fun NavController.openVN(vn: VN, transitionView: View) = navigate(
    NavigationDirections.openVN(vn.id, vn.image, vn.image_nsfw),
    FragmentNavigatorExtras(transitionView to (ViewCompat.getTransitionName(transitionView) ?: ""))
)