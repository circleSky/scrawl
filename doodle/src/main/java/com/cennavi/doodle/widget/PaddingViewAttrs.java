package com.cennavi.doodle.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import com.cennavi.doodle.R.styleable;
public class PaddingViewAttrs {
    public PaddingViewAttrs() {
    }

    @SuppressLint({"ResourceType"})
    public static void obtainsAttrs(Context context, View view, AttributeSet attrs) {
        int[] systemAttrs = new int[]{16842996, 16842997};
        TypedArray array = context.obtainStyledAttributes(attrs, systemAttrs);
        TypedValue typedValue = new TypedValue();
        array.getValue(0, typedValue);
        if (typedValue.type == 5) {
            array.getValue(1, typedValue);
            if (typedValue.type == 5) {
                int width = array.getDimensionPixelOffset(0, 0);
                int height = array.getDimensionPixelOffset(1, 0);
                array.recycle();
                if (width > 0 && height > 0) {
                    TypedArray a = context.obtainStyledAttributes(attrs, styleable.PaddingViewAttrs);
                    int mContentWidth = a.getDimensionPixelOffset(styleable.PaddingViewAttrs_vp_content_width, 0);
                    int mContentHeight = a.getDimensionPixelOffset(styleable.PaddingViewAttrs_vp_content_height, 0);
                    int mContentPaddingLeft = a.getDimensionPixelOffset(styleable.PaddingViewAttrs_vp_content_padding_left, -1);
                    int mContentPaddingTop = a.getDimensionPixelOffset(styleable.PaddingViewAttrs_vp_content_padding_top, -1);
                    int mContentPaddingRight = a.getDimensionPixelOffset(styleable.PaddingViewAttrs_vp_content_padding_right, -1);
                    int mContentPaddingBottom = a.getDimensionPixelOffset(styleable.PaddingViewAttrs_vp_content_padding_bottom, -1);
                    a.recycle();
                    if (mContentWidth > 0 && mContentHeight > 0 && mContentWidth <= width && mContentHeight <= height) {
                        int paddingLeft = (int)((float)(width - mContentWidth) / 2.0F + 0.5F);
                        int paddingRight = paddingLeft;
                        int paddingTop = (int)((float)(height - mContentHeight) / 2.0F + 0.5F);
                        int paddingBottom = paddingTop;
                        if (mContentPaddingLeft >= 0) {
                            paddingRight = paddingLeft + paddingLeft - mContentPaddingLeft;
                            paddingLeft = mContentPaddingLeft;
                        } else if (mContentPaddingRight >= 0) {
                            paddingLeft = paddingLeft + paddingLeft - mContentPaddingRight;
                            paddingRight = mContentPaddingRight;
                        }

                        if (mContentPaddingTop >= 0) {
                            paddingBottom = paddingTop + paddingTop - mContentPaddingTop;
                            paddingTop = mContentPaddingTop;
                        } else if (mContentPaddingBottom >= 0) {
                            paddingTop = paddingTop + paddingTop - mContentPaddingBottom;
                            paddingBottom = mContentPaddingBottom;
                        }

                        if (paddingLeft != view.getPaddingLeft() || paddingTop != view.getPaddingTop() || paddingRight != view.getPaddingRight() || paddingBottom != view.getPaddingBottom()) {
                            view.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
                        }

                    }
                }
            }
        }
    }
}
