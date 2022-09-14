package com.cennavi.doodle.image;

import android.graphics.Bitmap;
import android.view.View;

public interface ImageLoader {
    boolean load(View var1, String var2, ImageLoaderConfig var3, ImageLoader.ImageLoaderListener var4);

    boolean load(String var1, ImageLoaderConfig var2, ImageLoader.ImageLoaderListener var3);

    public abstract static class ImageLoaderListener {
        public static final int FAILED_TASK_EXISTS = -1;
        public static final int FAILED_TASK_CANCELLED = -2;
        public static final int FAILED_BITMAP_ERROR = -3;

        public ImageLoaderListener() {
        }

        public void onLoadStarted(String path, ImageLoaderConfig config) {
        }

        public void onLoading(String path, ImageLoaderConfig config, long total, long current) {
        }

        public abstract void onLoadCompleted(String var1, ImageLoaderConfig var2, Bitmap var3);

        public abstract void onLoadFailed(String var1, ImageLoaderConfig var2, int var3);
    }
}
