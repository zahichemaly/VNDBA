package com.booboot.vndbandroid.util.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.WindowInsets;
import android.widget.FrameLayout;

public class FrameLayoutKeyboardSupport extends FrameLayout {
    public FrameLayoutKeyboardSupport(Context context) {
        super(context);
    }

    public FrameLayoutKeyboardSupport(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FrameLayoutKeyboardSupport(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FrameLayoutKeyboardSupport(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        /* removes the paddings induced by fitsSystemWindows="true" on all children views */
        setPadding(0, 0, 0, insets.getSystemWindowInsetBottom());
        return insets.consumeSystemWindowInsets();
    }
}