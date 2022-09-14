package com.cennavi.doodle.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import com.cennavi.doodle.R.styleable;
public class STextView extends PaddingTextView {
    public STextView(Context context) {
        this(context, (AttributeSet)null);
    }

    public STextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public STextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray a = this.getContext().obtainStyledAttributes(attrs, styleable.STextView);
        int defaultColor = this.getTextColors().getDefaultColor();
        int textColorSelected = a.getColor(styleable.STextView_stv_text_color_selected, defaultColor);
        int textColorPressed = a.getColor(styleable.STextView_stv_text_color_pressed, a.getColor(styleable.STextView_mtv_text_color_pressed, defaultColor));
        int textColorDisable = a.getColor(styleable.STextView_stv_text_color_disable, a.getColor(styleable.STextView_mtv_text_color_disable, defaultColor));
        this.setTextColor(this.createColorStateList(textColorPressed, textColorSelected, textColorDisable, defaultColor));
        a.recycle();
        SelectorAttrs.obtainsAttrs(this.getContext(), this, attrs);
    }

    private ColorStateList createColorStateList(int pressed, int selected, int unable, int normal) {
        ColorStateList colorStateList = new ColorStateList(new int[][]{{16842910, 16842919}, {16842910, 16842913}, {-16842910}, new int[0]}, new int[]{pressed, selected, unable, normal});
        return colorStateList;
    }
}
