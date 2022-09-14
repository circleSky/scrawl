package com.cennavi.doodle.image;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
//import com.cennavi.doodle.imagepicker.ImageLoader.ImageLoaderListener;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class ImageLoaderGroup implements ImageLoader {
    private ImageCache mImageCache;
    private ImageLoaderConfig mImageLoaderConfig;
    private CopyOnWriteArrayList<ImageLoader> mImageLoaders;

    public ImageLoaderGroup(Context context) {
        this(context, (int)Runtime.getRuntime().maxMemory() / 8, 26214400L);
    }

    public ImageLoaderGroup(Context context, int memoryMaxSize, long diskMaxSize) {
        this(context, (ImageLoaderConfig)null);
        this.mImageCache = new ImageCache(context, memoryMaxSize, diskMaxSize);
        this.mImageLoaderConfig = new ImageLoaderConfig(this.mImageCache);
    }

    public ImageLoaderGroup(Context context, ImageLoaderConfig config) {
        this.mImageLoaders = new CopyOnWriteArrayList();
        this.mImageLoaderConfig = config;
    }

    public void setImageLoaderConfig(ImageLoaderConfig imageLoaderConfig) {
        this.mImageLoaderConfig = imageLoaderConfig;
    }

    public ImageLoaderConfig getImageLoaderConfig() {
        return this.mImageLoaderConfig;
    }

    public boolean load(View view, String path, ImageLoaderConfig config, ImageLoaderListener loaderListener) {
        if (view != null && !TextUtils.isEmpty(path)) {
            boolean accept = false;
            Iterator var6 = this.mImageLoaders.iterator();

            while(var6.hasNext()) {
                ImageLoader loader = (ImageLoader)var6.next();
                if (loader.load(view, path, config, loaderListener)) {
                    accept = true;
                    break;
                }
            }

            return accept;
        } else {
            return false;
        }
    }

    public boolean load(String path, ImageLoaderConfig config, ImageLoaderListener loaderListener) {
        if (TextUtils.isEmpty(path)) {
            return false;
        } else {
            boolean accept = false;
            Iterator var5 = this.mImageLoaders.iterator();

            while(var5.hasNext()) {
                ImageLoader loader = (ImageLoader)var5.next();
                if (loader.load(path, config, loaderListener)) {
                    accept = true;
                    break;
                }
            }

            return accept;
        }
    }

    public boolean load(View view, String path) {
        return this.load(view, path, this.mImageLoaderConfig, (com.cennavi.doodle.image.ImageLoader.ImageLoaderListener)null);
    }

    public boolean load(String path, ImageLoaderListener loaderListener) {
        return this.load(path, this.mImageLoaderConfig, loaderListener);
    }

    public void addImageLoader(ImageLoader loader) {
        if (loader != null) {
            this.mImageLoaders.add(loader);
        }
    }

    public void removeImageLoader(ImageLoader loader) {
        if (loader != null) {
            this.mImageLoaders.remove(loader);
        }
    }

    public boolean containImageLoader(ImageLoader loader) {
        return this.mImageLoaders.contains(loader);
    }

    public void clearAllImageLoaders() {
        this.mImageLoaders.clear();
    }

    public ImageCache getImageCache() {
        return this.mImageCache;
    }
}
