package com.cennavi.doodle.image;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;

import com.cennavi.doodle.util.ImageUtils;
import com.cennavi.doodle.util.LogUtil;
import com.cennavi.doodle.util.SimpleAsyncTask;
import com.cennavi.doodle.util.Util;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

public class LocalImagerLoader implements ImageLoader {
    private Context mContext;

    public LocalImagerLoader(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public boolean load(View view, String path, ImageLoaderConfig config, ImageLoaderListener loaderListener) {
        if (config == null) {
            throw new RuntimeException("ImageLoaderConfig is null!");
        } else if (!path.startsWith("/") && !path.startsWith("assets/")) {
            return false;
        } else {
            int[] size = ImageUtils.optimizeMaxSizeByView(view, config.getMaxWidth(), config.getMaxHeight());
            int width = size[0];
            int height = size[1];
            String key = config.isNeedCache() ? config.getCacheKeyGenerator().generateCacheKey(size, path, config) : null;
            if (config.isNeedCache()) {
                Bitmap bitmap = config.getImageCache().getBitmapMemoryCache(key);
                if (bitmap != null) {
                    if (loaderListener != null) {
                        loaderListener.onLoadStarted(path, config);
                    }

                    if (view != null) {
                        config.getImageSetter().setImage(view, bitmap);
                    }

                    if (loaderListener != null) {
                        loaderListener.onLoadCompleted(path, config, bitmap);
                    }

                    return true;
                }
            }

            LocalImagerLoader.ImageLoadTask task;
            if (view != null) {
                if (cancelUselessImageLoadTask(view, path, config) == null) {
                    task = new LocalImagerLoader.ImageLoadTask(this.mContext, view, path, width, height, config, key, loaderListener);
                    config.getImageSetter().setImage(view, new AsyncDrawable(config.getLoadingDrawable(), task));
                    task.executePriority(config.getPriority(), new String[0]);
                } else if (loaderListener != null) {
                    loaderListener.onLoadFailed(path, config, -1);
                }
            } else {
                task = new LocalImagerLoader.ImageLoadTask(this.mContext, (View)null, path, width, height, config, key, loaderListener);
                task.executePriority(config.getPriority(), new String[0]);
            }

            return true;
        }
    }

    public boolean load(String path, ImageLoaderConfig config, ImageLoaderListener loaderListener) {
        return this.load((View)null, path, config, loaderListener);
    }

    public static Bitmap getBitmapFromDisk(FileDescriptor fileDescriptor, int maxWidth, int maxHeight, ImageCache imageCache, String key, Bitmap.CompressFormat format) {
        Bitmap bitmap = null;
        if (imageCache != null) {
            bitmap = imageCache.getBitmapDiskCache(key);
            if (bitmap != null) {
                return bitmap;
            }
        }

        BitmapFactory.Options options = null;

        try {
            options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFileDescriptor(fileDescriptor, (Rect)null, options);
            int h = options.outHeight;
            int w = options.outWidth;
            options.inSampleSize = ImageUtils.computeBitmapSimple(w * h, maxWidth * maxHeight * 2);
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inInputShareable = true;
            options.inPurgeable = true;
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor, (Rect)null, options);
        } catch (Throwable var10) {
            LogUtil.i("get bitmap error");
            return null;
        }

        if (imageCache != null && bitmap != null) {
            imageCache.saveBitmapDiskCache(bitmap, key, format);
        }

        return bitmap;
    }

    private static LocalImagerLoader.ImageLoadTask cancelUselessImageLoadTask(View view, String key, ImageLoaderConfig config) {
        LocalImagerLoader.ImageLoadTask oldLoadTask = getLoadTaskFromContainer(view, config.getImageSetter());
        if (oldLoadTask != null) {
            String oldKey = oldLoadTask.getKey();
            if (!TextUtils.isEmpty(oldKey) && oldKey.equals(key)) {
                return oldLoadTask;
            } else {
                oldLoadTask.cancel(true);
                return null;
            }
        } else {
            return null;
        }
    }

    private static LocalImagerLoader.ImageLoadTask getLoadTaskFromContainer(View view, ImageLoaderConfig.ImageSetter setter) {
        if (view != null) {
            Drawable drawable = setter.getDrawable(view);
            if (drawable instanceof AsyncDrawable) {
                AsyncDrawable asyncDrawable = (AsyncDrawable)drawable;
                SimpleAsyncTask task = asyncDrawable.getBitmapWorkerTask();
                if (task instanceof LocalImagerLoader.ImageLoadTask) {
                    return (LocalImagerLoader.ImageLoadTask)task;
                }
            }
        }

        return null;
    }

    private static void animationDisplay(View container, Animation animation) {
        if (container != null && animation != null) {
            try {
                Method cloneMethod = Animation.class.getDeclaredMethod("clone");
                cloneMethod.setAccessible(true);
                container.startAnimation((Animation)cloneMethod.invoke(animation));
            } catch (Throwable var3) {
                container.startAnimation(animation);
            }

        }
    }

    private static class ImageLoadTask extends SimpleAsyncTask<String, Object, Bitmap> {
        private static final int PROGRESS_LOAD_STARTED = 0;
        private static final int PROGRESS_LOADING = 1;
        private Context mContext;
        private WeakReference<View> mViewRef;
        private String mPath;
        private int mMaxWidth;
        private int mMaxHeight;
        private ImageLoaderConfig mConfig;
        private String mKey;
        private ImageLoaderListener mLoaderListener;

        public ImageLoadTask(Context context, View view, String path, int maxWidth, int maxHeight, ImageLoaderConfig config, String key, ImageLoaderListener loaderListener) {
            this.mContext = context.getApplicationContext();
            this.mViewRef = view == null ? null : new WeakReference(view);
            this.mPath = path;
            this.mMaxWidth = maxWidth;
            this.mMaxHeight = maxHeight;
            this.mConfig = config;
            this.mKey = key;
            this.mLoaderListener = loaderListener;
        }

        private boolean abort() {
            if (this.mViewRef != null) {
                if (this.mViewRef.get() == null || this.isCancelled()) {
                    return true;
                }

                LocalImagerLoader.ImageLoadTask oldLoadTask = LocalImagerLoader.getLoadTaskFromContainer((View)this.mViewRef.get(), this.mConfig.getImageSetter());
                if (this != oldLoadTask) {
                    return true;
                }
            } else if (this.isCancelled()) {
                return true;
            }

            return false;
        }

        protected Bitmap doInBackground(String... params) {
            if (this.abort()) {
                return null;
            } else {
                try {
                    this.start();
                    FileInputStream fileInputStream = null;

                    Bitmap var4;
                    try {
                        if (this.mPath.startsWith("/")) {
                            fileInputStream = new FileInputStream(this.mPath);
                        } else if (this.mPath.startsWith("assets/")) {
                            AssetFileDescriptor assetFileDescriptor = this.mContext.getAssets().openFd(this.mPath.substring(7, this.mPath.length()));
                            fileInputStream = assetFileDescriptor.createInputStream();
                        }

                        Bitmap bm = LocalImagerLoader.getBitmapFromDisk(fileInputStream.getFD(), this.mConfig.isLoadOriginal() ? 0 : this.mMaxWidth, this.mConfig.isLoadOriginal() ? 0 : this.mMaxHeight, this.mConfig.isNeedCache() ? this.mConfig.getImageCache() : null, this.mKey, this.mPath.toLowerCase().endsWith(".png") ? Bitmap.CompressFormat.PNG : Bitmap.CompressFormat.JPEG);
                        if (!this.abort()) {
                            if (bm != null) {
                                if (this.mConfig.isExtractThumbnail()) {
                                    float scale;
                                    if (bm.getWidth() < bm.getHeight()) {
                                        scale = (float)this.mMaxWidth / (float)bm.getWidth();
                                    } else {
                                        scale = (float)this.mMaxHeight / (float)bm.getHeight();
                                    }

                                    if (scale <= 1.0F) {
                                        bm = ThumbnailUtils.extractThumbnail(bm, this.mMaxWidth, this.mMaxHeight, 2);
                                    }
                                }

                                if (this.mConfig.isAutoRotate()) {
                                    bm = ImageUtils.rotateBitmapByExif(bm, this.mPath, true);
                                }

                                if (this.mConfig.isNeedCache()) {
                                    this.mConfig.getImageCache().saveBitmapMemoryCache(bm, this.mKey);
                                }
                            }

                            var4 = bm;
                            return var4;
                        }

                        var4 = null;
                    } catch (Throwable var9) {
                        LogUtil.e("open file failed:" + this.mPath);
                        return null;
                    } finally {
                        Util.closeQuietly(fileInputStream);
                    }

                    return var4;
                } catch (Exception var11) {
                    var11.printStackTrace();
                    return null;
                }
            }
        }

        protected void onPostExecute(Bitmap bitmap) {
            if (this.abort()) {
                if (this.mLoaderListener != null) {
                    this.mLoaderListener.onLoadFailed(this.mPath, this.mConfig, -2);
                }

            } else {
                if (this.mViewRef != null) {
                    View view = (View)this.mViewRef.get();
                    if (view != null) {
                        if (bitmap != null) {
                            this.mConfig.getImageSetter().setImage(view, bitmap);
                            LocalImagerLoader.animationDisplay(view, this.mConfig.getAnimation());
                            if (this.mLoaderListener != null) {
                                this.mLoaderListener.onLoadCompleted(this.mPath, this.mConfig, bitmap);
                            }
                        } else {
                            this.mConfig.getImageSetter().setImage(view, this.mConfig.getLoadFailedDrawable());
                            if (this.mLoaderListener != null) {
                                this.mLoaderListener.onLoadFailed(this.mPath, this.mConfig, -3);
                            }
                        }
                    } else if (this.mLoaderListener != null) {
                        this.mLoaderListener.onLoadFailed(this.mPath, this.mConfig, -2);
                    }
                } else if (bitmap != null) {
                    if (this.mLoaderListener != null) {
                        this.mLoaderListener.onLoadCompleted(this.mPath, this.mConfig, bitmap);
                    }
                } else if (this.mLoaderListener != null) {
                    this.mLoaderListener.onLoadFailed(this.mPath, this.mConfig, -3);
                }

            }
        }

        private void start() {
            this.publishProgress(new Object[]{0});
        }

        public void updateProgress(long total, long current) {
            this.publishProgress(new Object[]{1, total, current});
        }

        protected void onProgressUpdate(Object... values) {
            if (this.mLoaderListener != null) {
                switch((Integer)values[0]) {
                    case 0:
                        this.mLoaderListener.onLoadStarted(this.mPath, this.mConfig);
                        break;
                    case 1:
                        if (values.length != 3) {
                            return;
                        }

                        this.mLoaderListener.onLoading(this.mPath, this.mConfig, (Long)values[1], (Long)values[2]);
                }

            }
        }

        public View getView() {
            return (View)this.mViewRef.get();
        }

        public String getPath() {
            return this.mPath;
        }

        public int getMaxWidth() {
            return this.mMaxWidth;
        }

        public int getMaxHeight() {
            return this.mMaxHeight;
        }

        public ImageLoaderConfig getConfig() {
            return this.mConfig;
        }

        public String getKey() {
            return this.mKey;
        }
    }
}
