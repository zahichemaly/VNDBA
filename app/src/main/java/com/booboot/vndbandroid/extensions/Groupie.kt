package com.booboot.vndbandroid.extensions

import android.view.View
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions
import kotlinx.android.extensions.LayoutContainer

// Need to specify ContainerOptions in order for caching to work.
// See: https://youtrack.jetbrains.com/oauth?state=%2Fissue%2FKT-28617
@ContainerOptions(cache = CacheImplementation.HASH_MAP)
open class CustomGroupieViewHolder(override val containerView: View) : GroupieViewHolder(containerView), LayoutContainer

abstract class CustomItem<T : CustomGroupieViewHolder> : Item<T> {

    constructor() : super()
    constructor(id: Long) : super(id)

    abstract override fun createViewHolder(itemView: View): T
}