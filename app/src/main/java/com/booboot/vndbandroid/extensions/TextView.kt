package com.booboot.vndbandroid.extensions

import android.view.inputmethod.EditorInfo.IME_ACTION_DONE
import android.view.inputmethod.EditorInfo.IME_ACTION_GO
import android.widget.TextView

fun TextView.preventLineBreak(_maxLines: Int = Int.MAX_VALUE) = apply {
    setSingleLine()
    setHorizontallyScrolling(false)
    maxLines = _maxLines
}

fun TextView.onSubmitListener(action: (Int) -> Unit) {
    setOnEditorActionListener { _, actionId, _ ->
        if (actionId in listOf(IME_ACTION_GO, IME_ACTION_DONE)) {
            action(actionId)
        }
        false
    }
}

fun TextView.setNumberOverflow(n: Int, overflow: Int = 99) {
    text = if (n > overflow) "$overflow+" else n.toString()
}