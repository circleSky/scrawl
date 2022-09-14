package com.cennavi.doodle.image;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;

import com.cennavi.doodle.util.SimpleAsyncTask;

import java.lang.ref.WeakReference;

public class AsyncDrawable extends Drawable {
    private final WeakReference<SimpleAsyncTask> mImageLoadTaskRef;
    private final Drawable baseDrawable;

    public AsyncDrawable(Drawable drawable, SimpleAsyncTask task) {
        if (task == null) {
            throw new IllegalArgumentException("SimpleAsyncTask is null!");
        } else {
            this.baseDrawable = drawable;
            this.mImageLoadTaskRef = new WeakReference(task);
        }
    }

    public SimpleAsyncTask getBitmapWorkerTask() {
        return (SimpleAsyncTask)this.mImageLoadTaskRef.get();
    }

    public void draw(Canvas canvas) {
        if (this.baseDrawable != null) {
            this.baseDrawable.draw(canvas);
        }

    }

    public void setAlpha(int i) {
        if (this.baseDrawable != null) {
            this.baseDrawable.setAlpha(i);
        }

    }

    public void setColorFilter(ColorFilter colorFilter) {
        if (this.baseDrawable != null) {
            this.baseDrawable.setColorFilter(colorFilter);
        }

    }

    public int getOpacity() {
        return this.baseDrawable == null ? PixelFormat.UNKNOWN : this.baseDrawable.getOpacity();
    }

    public void setBounds(int left, int top, int right, int bottom) {
        if (this.baseDrawable != null) {
            this.baseDrawable.setBounds(left, top, right, bottom);
        }

    }

    public void setBounds(Rect bounds) {
        if (this.baseDrawable != null) {
            this.baseDrawable.setBounds(bounds);
        }

    }

    public void setChangingConfigurations(int configs) {
        if (this.baseDrawable != null) {
            this.baseDrawable.setChangingConfigurations(configs);
        }

    }

    public int getChangingConfigurations() {
        return this.baseDrawable == null ? 0 : this.baseDrawable.getChangingConfigurations();
    }

    public void setDither(boolean dither) {
        if (this.baseDrawable != null) {
            this.baseDrawable.setDither(dither);
        }

    }

    public void setFilterBitmap(boolean filter) {
        if (this.baseDrawable != null) {
            this.baseDrawable.setFilterBitmap(filter);
        }

    }

    public void invalidateSelf() {
        if (this.baseDrawable != null) {
            this.baseDrawable.invalidateSelf();
        }

    }

    public void scheduleSelf(Runnable what, long when) {
        if (this.baseDrawable != null) {
            this.baseDrawable.scheduleSelf(what, when);
        }

    }

    public void unscheduleSelf(Runnable what) {
        if (this.baseDrawable != null) {
            this.baseDrawable.unscheduleSelf(what);
        }

    }

    public void setColorFilter(int color, PorterDuff.Mode mode) {
        if (this.baseDrawable != null) {
            this.baseDrawable.setColorFilter(color, mode);
        }

    }

    public void clearColorFilter() {
        if (this.baseDrawable != null) {
            this.baseDrawable.clearColorFilter();
        }

    }

    public boolean isStateful() {
        return this.baseDrawable != null && this.baseDrawable.isStateful();
    }

    public boolean setState(int[] stateSet) {
        return this.baseDrawable != null && this.baseDrawable.setState(stateSet);
    }

    public int[] getState() {
        return this.baseDrawable == null ? null : this.baseDrawable.getState();
    }

    public Drawable getCurrent() {
        return this.baseDrawable == null ? null : this.baseDrawable.getCurrent();
    }

    public boolean setVisible(boolean visible, boolean restart) {
        return this.baseDrawable != null && this.baseDrawable.setVisible(visible, restart);
    }

    public Region getTransparentRegion() {
        return this.baseDrawable == null ? null : this.baseDrawable.getTransparentRegion();
    }

    public int getIntrinsicWidth() {
        return this.baseDrawable == null ? 0 : this.baseDrawable.getIntrinsicWidth();
    }

    public int getIntrinsicHeight() {
        return this.baseDrawable == null ? 0 : this.baseDrawable.getIntrinsicHeight();
    }

    public int getMinimumWidth() {
        return this.baseDrawable == null ? 0 : this.baseDrawable.getMinimumWidth();
    }

    public int getMinimumHeight() {
        return this.baseDrawable == null ? 0 : this.baseDrawable.getMinimumHeight();
    }

    public boolean getPadding(Rect padding) {
        return this.baseDrawable != null && this.baseDrawable.getPadding(padding);
    }

    public Drawable mutate() {
        return this.baseDrawable == null ? null : this.baseDrawable.mutate();
    }

    public ConstantState getConstantState() {
        return this.baseDrawable == null ? null : this.baseDrawable.getConstantState();
    }
}
