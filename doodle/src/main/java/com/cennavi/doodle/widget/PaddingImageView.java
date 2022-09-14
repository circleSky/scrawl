package com.cennavi.doodle.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

@SuppressLint("AppCompatCustomView")
public class PaddingImageView extends ImageView {
    public PaddingImageView(Context context) {
        this(context, (AttributeSet)null);
    }

    public PaddingImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PaddingImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(attrs);
    }

    private void init(AttributeSet attrs) {
        PaddingViewAttrs.obtainsAttrs(this.getContext(), this, attrs);
    }
}
