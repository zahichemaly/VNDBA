package com.booboot.vndbandroid.util.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.extensions.toggle
import kotlinx.android.synthetic.main.background_info.view.*

class BackgroundInfo @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    init {
        inflate(context, R.layout.background_info, this)

        attrs?.let {
            with(context.obtainStyledAttributes(it, R.styleable.BackgroundInfo)) {
                text.text = getString(R.styleable.BackgroundInfo_text)
                button.toggle(getBoolean(R.styleable.BackgroundInfo_buttonVisible, false))
                button.text = getString(R.styleable.BackgroundInfo_buttonText)
                animationView.setMaxFrame(getInteger(R.styleable.BackgroundInfo_maxFrame, animationView.maxFrame.toInt()))
                recycle()
            }
        }
    }
}