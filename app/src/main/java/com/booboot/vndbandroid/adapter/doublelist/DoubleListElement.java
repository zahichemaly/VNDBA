package com.booboot.vndbandroid.adapter.doublelist;

/**
 * Created by od on 12/04/2016.
 */
public class DoubleListElement {
    private String leftText;
    private String rightText;
    private boolean displayRightTextOnly;

    public DoubleListElement(String leftText, String rightText, boolean displayRightTextOnly) {
        this.leftText = leftText;
        this.rightText = rightText;
        this.displayRightTextOnly = displayRightTextOnly;
    }

    public String getLeftText() {
        return leftText;
    }

    public void setLeftText(String leftText) {
        this.leftText = leftText;
    }

    public String getRightText() {
        return rightText;
    }

    public void setRightText(String rightText) {
        this.rightText = rightText;
    }

    public boolean isDisplayRightTextOnly() {
        return displayRightTextOnly;
    }

    public void setDisplayRightTextOnly(boolean displayRightTextOnly) {
        this.displayRightTextOnly = displayRightTextOnly;
    }
}
