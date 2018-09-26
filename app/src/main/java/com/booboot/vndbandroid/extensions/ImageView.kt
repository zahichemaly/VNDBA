package com.booboot.vndbandroid.extensions

import android.view.View
import android.widget.ImageView
import com.booboot.vndbandroid.App
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.model.vndbandroid.Preferences
import com.booboot.vndbandroid.util.image.DarkBlurTransform
import com.squareup.picasso.Picasso

fun ImageView.showNsfwImage(image: String?, imageNsfw: Boolean, nsfwTag: View) {
    val nsfw = imageNsfw && !Preferences.nsfw
    val picasso = if (nsfw) Picasso.get().load(R.drawable.nsfw_background) else Picasso.get().load(image)
    picasso.transform(DarkBlurTransform(App.context, nsfw)).into(this)
    nsfwTag.toggle(nsfw)
}