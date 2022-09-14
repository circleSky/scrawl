package com.cennavi.doodle.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.util.AttributeSet;

import com.cennavi.doodle.R.styleable;

public class MaskImageView extends PaddingImageView {
    public static final int MASK_LEVEL_BACKGROUND = 1;
    public static final int MASK_LEVEL_FOREGROUND = 2;
    private boolean mIsIgnoreAlpha;
    private boolean mIsShowMaskOnClick;
    private int mMaskColor;
    private float mPressedAlpha;
    private int mMaskLevel;
    private ColorMatrix mColorMatrix;
    private ColorFilter mColorFilter;
    private ColorFilter mLastColorFilter;

    public MaskImageView(Context context) {
        this(context, (AttributeSet)null);
    }

    public MaskImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaskImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mIsIgnoreAlpha = true;
        this.mIsShowMaskOnClick = true;
        this.mMaskColor = 16777215;
        this.mPressedAlpha = 1.0F;
        this.mMaskLevel = 2;
        this.mColorMatrix = new ColorMatrix();
        this.init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray a = this.getContext().obtainStyledAttributes(attrs, styleable.MaskImageView);
        this.mIsIgnoreAlpha = a.getBoolean(styleable.MaskImageView_miv_is_ignore_alpha, this.mIsIgnoreAlpha);
        this.mIsShowMaskOnClick = a.getBoolean(styleable.MaskImageView_miv_is_show_mask_on_click, this.mIsShowMaskOnClick);
        this.mMaskColor = a.getColor(styleable.MaskImageView_miv_mask_color, this.mMaskColor);
        this.mMaskLevel = a.getInt(styleable.MaskImageView_miv_mask_level, this.mMaskLevel);
        this.mPressedAlpha = a.getFloat(styleable.MaskImageView_miv_pressed_alpha, this.mPressedAlpha);
        this.setMaskColor(this.mMaskColor);
        a.recycle();
        SelectorAttrs.obtainsAttrs(this.getContext(), this, attrs);
    }

    private void setColorMatrix(float[] matrix) {
        this.mColorMatrix.set(matrix);
        this.mColorFilter = new ColorMatrixColorFilter(this.mColorMatrix);
    }

    private void setDrawableColorFilter(ColorFilter colorFilter) {
        if (this.mMaskLevel == 1) {
            if (this.getBackground() != null) {
                if (this.mLastColorFilter == colorFilter) {
                    return;
                }

                this.getBackground().mutate();
                this.getBackground().setColorFilter(colorFilter);
            }
        } else if (this.mMaskLevel == 2 && this.getDrawable() != null) {
            if (this.mLastColorFilter == colorFilter) {
                return;
            }

            this.getDrawable().mutate();
            this.getDrawable().setColorFilter(colorFilter);
        }

        this.mLastColorFilter = colorFilter;
    }

    public void draw(Canvas canvas) {
        if (this.mIsIgnoreAlpha) {
            if (this.mIsShowMaskOnClick && this.isPressed()) {
                this.setDrawableColorFilter(this.mColorFilter);
            } else {
                this.setDrawableColorFilter((ColorFilter)null);
            }
        }

        super.draw(canvas);
    }

    protected void onDraw(Canvas canvas) {
        if (!this.mIsIgnoreAlpha) {
            this.setDrawableColorFilter((ColorFilter)null);
            if (this.mMaskLevel == 1) {
                if (this.mIsShowMaskOnClick && this.isPressed()) {
                    canvas.drawColor(this.mMaskColor);
                }

                super.onDraw(canvas);
            } else {
                super.onDraw(canvas);
                if (this.mIsShowMaskOnClick && this.isPressed()) {
                    canvas.drawColor(this.mMaskColor);
                }
            }
        } else {
            super.onDraw(canvas);
        }

    }

    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
        if (pressed) {
            this.setAlpha(this.mPressedAlpha);
        } else {
            this.setAlpha(1.0F);
        }

    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        this.invalidate();
    }

    public boolean isIsIgnoreAlpha() {
        return this.mIsIgnoreAlpha;
    }

    public void setIsIgnoreAlpha(boolean mIsIgnoreAlpha) {
        this.mIsIgnoreAlpha = mIsIgnoreAlpha;
        this.invalidate();
    }

    public boolean isIsShowMaskOnClick() {
        return this.mIsShowMaskOnClick;
    }

    public void setIsShowMaskOnClick(boolean isShowMaskOnClick) {
        this.mIsShowMaskOnClick = isShowMaskOnClick;
        this.invalidate();
    }

    public int getShadeColor() {
        return this.getMaskColor();
    }

    public void setShadeColor(int shadeColor) {
        this.setMaskColor(shadeColor);
    }

    public int getMaskColor() {
        return this.mMaskColor;
    }

    public void setMaskColor(int maskColor) {
        this.mMaskColor = maskColor;
        float r = (float) Color.alpha(maskColor) / 255.0F;
        r -= (1.0F - r) * 0.15F;
        float rr = (1.0F - r) * 1.15F;
        this.setColorMatrix(new float[]{rr, 0.0F, 0.0F, 0.0F, (float)Color.red(maskColor) * r, 0.0F, rr, 0.0F, 0.0F, (float)Color.green(maskColor) * r, 0.0F, 0.0F, rr, 0.0F, (float)Color.blue(maskColor) * r, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F});
        this.invalidate();
    }

    public int getMaskLevel() {
        return this.mMaskLevel;
    }

    public void setMaskLevel(int maskLevel) {
        this.mMaskLevel = maskLevel;
        this.invalidate();
    }

    public void setPressedAlpha(float pressedAlpha) {
        this.mPressedAlpha = pressedAlpha;
        this.invalidate();
    }
}
