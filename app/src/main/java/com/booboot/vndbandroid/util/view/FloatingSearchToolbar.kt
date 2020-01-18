package com.booboot.vndbandroid.util.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.annotation.AttrRes
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.extensions.dimen
import com.booboot.vndbandroid.extensions.setPaddingTop
import com.booboot.vndbandroid.extensions.setTextChangedListener
import com.booboot.vndbandroid.extensions.statusBarHeight
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
import kotlinx.android.synthetic.main.floating_search_toolbar.view.*
import kotlin.math.roundToInt

class FloatingSearchToolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : AppBarLayout(context, attrs, defStyleAttr) {
    private val onTextChangedDefault: (String) -> Unit = {
        searchBarTextInputLayout.isEndIconVisible = it.isNotEmpty()
    }

    init {
        inflate(context, R.layout.floating_search_toolbar, this)

        setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))

        attrs?.let {
            with(context.obtainStyledAttributes(it, R.styleable.FloatingSearchToolbar)) {
                searchBarTextInputLayout.startIconDrawable = getDrawable(R.styleable.FloatingSearchToolbar_startIcon)
                searchBar.hint = getString(R.styleable.FloatingSearchToolbar_hintText)
                searchBarCardView.updateLayoutParams<LayoutParams> {
                    if (getBoolean(R.styleable.FloatingSearchToolbar_hideOnScroll, false)) {
                        scrollFlags = scrollFlags or SCROLL_FLAG_SCROLL
                    }
                }
                recycle()
            }
        }
    }

    fun setupWithContainer(container: ViewGroup, scrollingChild: View) {
        val paddingTop = statusBarHeight() + dimen(R.dimen.floating_search_bar_height)
        scrollingChild.setPaddingTop(paddingTop)
        container.updateLayoutParams<MarginLayoutParams> {
            topMargin = -paddingTop
        }
        val refreshLayout = container.findViewById<SwipeRefreshLayout>(R.id.refreshLayout)
        refreshLayout?.setProgressViewOffset(false, refreshLayout.progressViewStartOffset + paddingTop, (refreshLayout.progressViewEndOffset + 0.75 * paddingTop).roundToInt())

        searchBarTextInputLayout.isEndIconVisible = false
        setTextChangedListener()
        searchBarTextInputLayout.setEndIconOnClickListener {
            searchBar.text = null
        }
    }

    fun setTextChangedListener(onTextChanged: (String) -> Unit = {}) = searchBar.setTextChangedListener {
        onTextChangedDefault(it)
        onTextChanged(it)
    }
}