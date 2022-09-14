package com.cennavi.doodle.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class PaddingTextView extends TextView {
    public PaddingTextView(Context context) {
        this(context, (AttributeSet)null);
    }

    public PaddingTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PaddingTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(attrs);
    }

    private void init(AttributeSet attrs) {
        PaddingViewAttrs.obtainsAttrs(this.getContext(), this, attrs);
    }
}
