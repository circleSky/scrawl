package com.cennavi.doodle.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class SLinearLayout extends LinearLayout {
    public SLinearLayout(Context context) {
        this(context, (AttributeSet)null);
    }

    public SLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(attrs);
    }

    private void init(AttributeSet attrs) {
        SelectorAttrs.obtainsAttrs(this.getContext(), this, attrs);
    }
}
