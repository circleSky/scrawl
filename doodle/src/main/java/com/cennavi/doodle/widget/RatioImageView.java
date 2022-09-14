package com.cennavi.doodle.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.cennavi.doodle.R.styleable;

@SuppressLint("AppCompatCustomView")
public class RatioImageView extends ImageView {
    private float mDrawableSizeRatio;
    private boolean mIsWidthFitDrawableSizeRatio;
    private boolean mIsHeightFitDrawableSizeRatio;
    private int mMaxWidthWhenWidthFixDrawable;
    private int mMaxHeightWhenHeightFixDrawable;
    private float mWidthRatio;
    private float mHeightRatio;
    private int mDesiredWidth;
    private int mDesiredHeight;

    public RatioImageView(Context context) {
        this(context, (AttributeSet)null);
    }

    public RatioImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mDrawableSizeRatio = -1.0F;
        this.mMaxWidthWhenWidthFixDrawable = -1;
        this.mMaxHeightWhenHeightFixDrawable = -1;
        this.mWidthRatio = -1.0F;
        this.mHeightRatio = -1.0F;
        this.mDesiredWidth = -1;
        this.mDesiredHeight = -1;
        this.init(attrs);
        if (this.getDrawable() != null) {
            this.mDrawableSizeRatio = 1.0F * (float)this.getDrawable().getIntrinsicWidth() / (float)this.getDrawable().getIntrinsicHeight();
        }

    }

    private void init(AttributeSet attrs) {
        TypedArray a = this.getContext().obtainStyledAttributes(attrs, styleable.RatioImageView);
        this.mIsWidthFitDrawableSizeRatio = a.getBoolean(styleable.RatioImageView_riv_is_width_fix_drawable_size_ratio, this.mIsWidthFitDrawableSizeRatio);
        this.mIsHeightFitDrawableSizeRatio = a.getBoolean(styleable.RatioImageView_riv_is_height_fix_drawable_size_ratio, this.mIsHeightFitDrawableSizeRatio);
        this.mMaxWidthWhenWidthFixDrawable = a.getDimensionPixelOffset(styleable.RatioImageView_riv_max_width_when_width_fix_drawable, this.mMaxWidthWhenWidthFixDrawable);
        this.mMaxHeightWhenHeightFixDrawable = a.getDimensionPixelOffset(styleable.RatioImageView_riv_max_height_when_height_fix_drawable, this.mMaxHeightWhenHeightFixDrawable);
        this.mHeightRatio = a.getFloat(styleable.RatioImageView_riv_height_to_width_ratio, this.mHeightRatio);
        this.mWidthRatio = a.getFloat(styleable.RatioImageView_riv_width_to_height_ratio, this.mWidthRatio);
        this.mDesiredWidth = a.getDimensionPixelOffset(styleable.RatioImageView_riv_width, this.mDesiredWidth);
        this.mDesiredHeight = a.getDimensionPixelOffset(styleable.RatioImageView_riv_height, this.mDesiredHeight);
        a.recycle();
        SelectorAttrs.obtainsAttrs(this.getContext(), this, attrs);
    }

    private void onSetDrawable() {
        Drawable drawable = this.getDrawable();
        if (drawable != null && (this.mIsWidthFitDrawableSizeRatio || this.mIsHeightFitDrawableSizeRatio)) {
            float old = this.mDrawableSizeRatio;
            this.mDrawableSizeRatio = 1.0F * (float)drawable.getIntrinsicWidth() / (float)drawable.getIntrinsicHeight();
            if (old != this.mDrawableSizeRatio && this.mDrawableSizeRatio > 0.0F) {
                this.requestLayout();
            }
        }

    }

    public void setImageResource(int resId) {
        super.setImageResource(resId);
        this.onSetDrawable();
    }

    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        this.onSetDrawable();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.mDrawableSizeRatio > 0.0F) {
            if (this.mIsWidthFitDrawableSizeRatio) {
                this.mWidthRatio = this.mDrawableSizeRatio;
            } else if (this.mIsHeightFitDrawableSizeRatio) {
                this.mHeightRatio = 1.0F / this.mDrawableSizeRatio;
            }
        }

        if (this.mHeightRatio > 0.0F && this.mWidthRatio > 0.0F) {
            throw new RuntimeException("高度和宽度不能同时设置百分比！！");
        } else {
            int height;
            int width;
            if (this.mWidthRatio > 0.0F) {
                width = 0;
                if (this.mDesiredHeight > 0) {
                    width = this.mDesiredHeight;
                } else {
                    width = MeasureSpec.getSize(heightMeasureSpec);
                }

                if (width <= 0) {
                    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                    return;
                }

                height = (int)((float)width * this.mWidthRatio);
                if (this.mIsWidthFitDrawableSizeRatio && this.mMaxWidthWhenWidthFixDrawable > 0 && height > this.mMaxWidthWhenWidthFixDrawable) {
                    height = this.mMaxWidthWhenWidthFixDrawable;
                    width = (int)((float)height / this.mWidthRatio);
                }

                super.onMeasure(MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY));
            } else if (this.mHeightRatio > 0.0F) {
                width = 0;
                if (this.mDesiredWidth > 0) {
                    width = this.mDesiredWidth;
                } else {
                    width = MeasureSpec.getSize(widthMeasureSpec);
                }

                if (width <= 0) {
                    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                    return;
                }

                height = (int)((float)width * this.mHeightRatio);
                if (this.mIsHeightFitDrawableSizeRatio && this.mMaxHeightWhenHeightFixDrawable > 0 && height > this.mMaxHeightWhenHeightFixDrawable) {
                    height = this.mMaxHeightWhenHeightFixDrawable;
                    width = (int)((float)height / this.mHeightRatio);
                }

                super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
            } else if (this.mDesiredHeight > 0 && this.mDesiredWidth > 0) {
                width = this.mDesiredWidth;
                height = this.mDesiredHeight;
                super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
            } else {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }

        }
    }

    public void setIsFitDrawableSizeRatio(boolean isWidthFitDrawableSizeRatio, boolean isHeightFitDrawableSizeRatio) {
        this.mWidthRatio = this.mHeightRatio = -1.0F;
        boolean oldIsWidth = this.mIsWidthFitDrawableSizeRatio;
        boolean oldIsHeight = this.mIsHeightFitDrawableSizeRatio;
        this.mIsWidthFitDrawableSizeRatio = isWidthFitDrawableSizeRatio;
        this.mIsHeightFitDrawableSizeRatio = isHeightFitDrawableSizeRatio;
        Drawable drawable = this.getDrawable();
        if (drawable != null) {
            this.mDrawableSizeRatio = 1.0F * (float)drawable.getIntrinsicWidth() / (float)drawable.getIntrinsicHeight();
        } else {
            this.mDrawableSizeRatio = -1.0F;
        }

        if (oldIsWidth != this.mIsWidthFitDrawableSizeRatio || oldIsHeight != this.mIsHeightFitDrawableSizeRatio) {
            this.requestLayout();
        }

    }

    public void setWidthRatio(float mWidthRatio) {
        this.mIsWidthFitDrawableSizeRatio = this.mIsHeightFitDrawableSizeRatio = false;
        float oldHeightRatio = this.mHeightRatio;
        this.mHeightRatio = -1.0F;
        this.mWidthRatio = mWidthRatio;
        if (mWidthRatio != mWidthRatio || oldHeightRatio != this.mHeightRatio) {
            this.requestLayout();
        }

    }

    public void setHeightRatio(float mHeightRatio) {
        this.mIsWidthFitDrawableSizeRatio = this.mIsHeightFitDrawableSizeRatio = false;
        float oldWidthRatio = this.mWidthRatio;
        this.mWidthRatio = -1.0F;
        this.mHeightRatio = mHeightRatio;
        if (oldWidthRatio != this.mWidthRatio || mHeightRatio != mHeightRatio) {
            this.requestLayout();
        }

    }

    public void setWidthAndHeight(int width, int height) {
        int oldW = this.mDesiredWidth;
        int oldH = this.mDesiredHeight;
        this.mDesiredWidth = width;
        this.mDesiredHeight = height;
        if (oldW != this.mDesiredWidth || oldH != this.mDesiredHeight) {
            this.requestLayout();
        }

    }

    public boolean isIsWidthFitDrawableSizeRatio() {
        return this.mIsWidthFitDrawableSizeRatio;
    }

    public boolean isIsHeightFitDrawableSizeRatio() {
        return this.mIsHeightFitDrawableSizeRatio;
    }

    public float getWidthRatio() {
        return this.mWidthRatio;
    }

    public float getHeightRatio() {
        return this.mHeightRatio;
    }

    public float getDrawableSizeRatio() {
        return this.mDrawableSizeRatio;
    }
}
