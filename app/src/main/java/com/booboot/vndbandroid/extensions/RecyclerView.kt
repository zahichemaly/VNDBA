package com.booboot.vndbandroid.extensions

import androidx.recyclerview.widget.RecyclerView
import android.view.MotionEvent

fun RecyclerView.disallowVerticalScrollIntercepts() {
    var x1 = 0f
    var x2: Float
    var y1 = 0f
    var y2: Float
    var dx: Float
    var dy: Float
    setOnTouchListener({ v, event ->
        val action = event.action
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                x1 = event.x
                y1 = event.y
            }

            MotionEvent.ACTION_MOVE -> {
                x2 = event.x
                y2 = event.y
                dx = x2 - x1
                dy = y2 - y1

                if (Math.abs(dx) > Math.abs(dy)) {
                    v.parent.requestDisallowInterceptTouchEvent(true)
                }
            }

            MotionEvent.ACTION_UP -> {
                v.parent.requestDisallowInterceptTouchEvent(false)
            }
        }

        v.onTouchEvent(event)
        true
    })
}