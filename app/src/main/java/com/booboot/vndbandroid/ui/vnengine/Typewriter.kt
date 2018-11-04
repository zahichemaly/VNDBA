package com.booboot.vndbandroid.ui.vnengine

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class Typewriter : AppCompatTextView {
    private var fullText: CharSequence = ""
    private var index: Int = 0
    var delay: Long = 25 // default 25ms delay

    private val characterHandler = Handler()
    private val characterAdder = object : Runnable {
        override fun run() {
            text = fullText.subSequence(0, index++)
            if (index <= fullText.length) {
                characterHandler.postDelayed(this, delay)
            }
        }
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun animateText(text: CharSequence) {
        fullText = text
        index = 0

        setText("")
        characterHandler.removeCallbacks(characterAdder)
        characterHandler.postDelayed(characterAdder, delay)
    }
}