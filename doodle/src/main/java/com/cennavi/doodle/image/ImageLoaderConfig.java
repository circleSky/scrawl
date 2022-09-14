package com.cennavi.doodle.image;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.cennavi.doodle.util.Priority;

public class ImageLoaderConfig {
    private static ImageLoaderConfig sDefaultConfig = new ImageLoaderConfig((ImageCache)null);
    private static ImageLoaderConfig.ImageSetter sDefaultImageSetter = new ImageLoaderConfig.ImageSetter();
    private static final ImageCacheKeyGenerator DEFAULT_KEY_GENERATOR = new ImageCacheKeyGenerator();
    private ImageCache mImageCache;
    private int mMaxWidth;
    private int mMaxHeight;
    private boolean mNeedCache;
    private boolean mLoadOriginal;
    private boolean mAutoRotate;
    private Animation mAnimation;
    private Drawable mLoadingDrawable;
    private Drawable mLoadFailedDrawable;
    private Bitmap.Config bitmapConfig;
    private ImageLoaderConfig.ImageSetter mImageSetter;
    private Priority mPriority;
    private boolean mExtractThumbnail;
    private ImageCacheKeyGenerator mCacheKeyGenerator;

    public static ImageLoaderConfig.ImageSetter getDefaultImageSetter() {
        return sDefaultImageSetter;
    }

    public static void setDefaultImageSetter(ImageLoaderConfig.ImageSetter defaultImageSetter) {
        sDefaultImageSetter = defaultImageSetter;
    }

    public ImageLoaderConfig() {
        this((ImageCache)null);
    }

    public ImageLoaderConfig(ImageCache imageCache) {
        this.mNeedCache = true;
        this.bitmapConfig = Bitmap.Config.RGB_565;
        this.mImageSetter = sDefaultImageSetter;
        this.mPriority = Priority.DEFAULT;
        this.mExtractThumbnail = false;
        this.mCacheKeyGenerator = DEFAULT_KEY_GENERATOR;
        this.mImageCache = imageCache;
    }

    public int getMaxWidth() {
        return this.mMaxWidth;
    }

    public void setMaxWidth(int maxWidth) {
        this.mMaxWidth = maxWidth;
    }

    public int getMaxHeight() {
        return this.mMaxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.mMaxHeight = maxHeight;
    }

    public boolean isNeedCache() {
        return this.mImageCache == null ? false : this.mNeedCache;
    }

    public void setNeedCache(boolean needCache) {
        this.mNeedCache = needCache;
    }

    public boolean isLoadOriginal() {
        return this.mLoadOriginal;
    }

    public void setLoadOriginal(boolean loadOriginal) {
        this.mLoadOriginal = loadOriginal;
    }

    public boolean isAutoRotate() {
        return this.mAutoRotate;
    }

    public void setAutoRotate(boolean autoRotate) {
        this.mAutoRotate = autoRotate;
    }

    public Animation getAnimation() {
        return this.mAnimation;
    }

    public void setAnimation(Animation animation) {
        this.mAnimation = animation;
    }

    public Drawable getLoadingDrawable() {
        return this.mLoadingDrawable;
    }

    public void setLoadingDrawable(Drawable loadingDrawable) {
        this.mLoadingDrawable = loadingDrawable;
    }

    public Drawable getLoadFailedDrawable() {
        return this.mLoadFailedDrawable;
    }

    public void setLoadFailedDrawable(Drawable loadFailedDrawable) {
        this.mLoadFailedDrawable = loadFailedDrawable;
    }

    public Bitmap.Config getBitmapConfig() {
        return this.bitmapConfig;
    }

    public void setBitmapConfig(Bitmap.Config bitmapConfig) {
        this.bitmapConfig = bitmapConfig;
    }

    public static ImageLoaderConfig getDefaultConfig() {
        return sDefaultConfig;
    }

    public static void setDefaultConfig(ImageLoaderConfig defaultConfig) {
        sDefaultConfig = defaultConfig;
    }

    public void setImageSetter(ImageLoaderConfig.ImageSetter imageSetter) {
        this.mImageSetter = imageSetter;
    }

    public ImageLoaderConfig.ImageSetter getImageSetter() {
        return this.mImageSetter;
    }

    public ImageCache getImageCache() {
        return this.mImageCache;
    }

    public void setImageCache(ImageCache imageCache) {
        this.mImageCache = imageCache;
    }

    public Priority getPriority() {
        return this.mPriority;
    }

    public void setPriority(Priority priority) {
        this.mPriority = priority;
    }

    public boolean isExtractThumbnail() {
        return this.mExtractThumbnail;
    }

    public void setExtractThumbnail(boolean extractThumbnail) {
        this.mExtractThumbnail = extractThumbnail;
    }

    public ImageLoaderConfig clone() {
        ImageLoaderConfig config = new ImageLoaderConfig(this.mImageCache);
        config.setAnimation(this.getAnimation());
        config.setAutoRotate(this.isAutoRotate());
        config.setBitmapConfig(this.getBitmapConfig());
        config.setImageSetter(this.getImageSetter());
        config.setLoadFailedDrawable(this.getLoadFailedDrawable());
        config.setLoadingDrawable(this.getLoadingDrawable());
        config.setLoadOriginal(this.isLoadOriginal());
        config.setMaxHeight(this.getMaxHeight());
        config.setMaxWidth(this.getMaxWidth());
        config.setPriority(this.getPriority());
        config.setNeedCache(this.isNeedCache());
        config.setImageCache(this.getImageCache());
        config.setCacheKeyGenerator(this.getCacheKeyGenerator());
        config.setExtractThumbnail(this.isExtractThumbnail());
        return config;
    }

    public void setCacheKeyGenerator(ImageCacheKeyGenerator keyGenerator) {
        this.mCacheKeyGenerator = keyGenerator;
    }

    public ImageCacheKeyGenerator getCacheKeyGenerator() {
        return this.mCacheKeyGenerator;
    }

    public static class ImageSetter {
        public ImageSetter() {
        }

        public void setImage(View view, Bitmap bitmap) {
            if (view != null) {
                if (view instanceof ImageView) {
                    ((ImageView)view).setImageBitmap(bitmap);
                } else {
                    view.setBackgroundDrawable(new BitmapDrawable(view.getResources(), bitmap));
                }

            }
        }

        public void setImage(View view, Drawable drawable) {
            if (view != null) {
                if (view instanceof ImageView) {
                    ((ImageView)view).setImageDrawable(drawable);
                } else {
                    view.setBackgroundDrawable(drawable);
                }

            }
        }

        public Drawable getDrawable(View view) {
            if (view == null) {
                return null;
            } else {
                return view instanceof ImageView ? ((ImageView)view).getDrawable() : view.getBackground();
            }
        }
    }
}
