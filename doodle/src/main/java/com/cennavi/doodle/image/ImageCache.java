package com.cennavi.doodle.image;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Build;
import android.os.Environment;
import android.util.LruCache;

import com.cennavi.doodle.cache.DiskLruCache;
import com.cennavi.doodle.util.LogUtil;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/3.
 */
public class ImageCache {
    private int mMemoryCacheMaxSize;
    private long mDiskCacheMaxSize;
    private File mDiskCacheDir;
    private final Object mDiskCacheLock;
    private DiskLruCache diskLruCache;
    private LruCache<String, Bitmap> memoryLruCache;
    private Context mContext;

    public ImageCache(Context context, int memoryCacheMaxSize, long diskCacheMaxSize) {
        this(context, memoryCacheMaxSize, diskCacheMaxSize, new File(getDiskCacheDir(context, "androidsCache")));
    }

    public ImageCache(Context context, int memoryCacheMaxSize, long diskCacheMaxSize, File diskCacheDir) {
        this.mDiskCacheLock = new Object();
        this.mContext = context;
        this.mMemoryCacheMaxSize = memoryCacheMaxSize;
        this.mDiskCacheMaxSize = diskCacheMaxSize;
        this.mDiskCacheDir = diskCacheDir;
        this.initMemoryCache();
        this.initDiskCache();
    }

    private void initMemoryCache() {
        this.memoryLruCache = new LruCache<String, Bitmap>(this.mMemoryCacheMaxSize) {
            protected int sizeOf(String key, Bitmap value) {
                return value == null ? 0 : value.getRowBytes() * value.getHeight();
            }
        };
    }

    private void initDiskCache() {
        synchronized(this.mDiskCacheLock) {
            if (this.diskLruCache == null || this.diskLruCache.isClosed()) {
                int vc = 0;

                try {
                    PackageManager manager = this.mContext.getPackageManager();
                    PackageInfo info = manager.getPackageInfo(this.mContext.getPackageName(), 0);
                    if (info != null) {
                        vc = info.versionCode;
                    }
                } catch (PackageManager.NameNotFoundException var6) {
                    var6.printStackTrace();
                }

                try {
                    if (!this.mDiskCacheDir.exists() && !this.mDiskCacheDir.mkdirs()) {
                        LogUtil.e("disk cache dir init failed");
                    } else {
                        this.diskLruCache = DiskLruCache.open(this.mDiskCacheDir, vc, 1, this.mDiskCacheMaxSize);
                    }
                } catch (Exception var7) {
                    var7.printStackTrace();
                }

            }
        }
    }

    public void clearAllCache() {
        this.clearAllMemoryCache();
        this.clearAllDiskCache();
    }

    public void clearAllMemoryCache() {
        if (this.memoryLruCache != null) {
            this.memoryLruCache.evictAll();
        }
    }

    public void clearAllDiskCache() {
        synchronized(this.mDiskCacheLock) {
            if (this.diskLruCache == null) {
                return;
            }

            try {
                this.diskLruCache.delete();
                this.diskLruCache.close();
                this.diskLruCache = null;
            } catch (IOException var4) {
                var4.printStackTrace();
            }
        }

        this.initDiskCache();
    }

    public void closeAllCache() {
        this.closeMemoryCache();
        this.closeDiskCache();
    }

    public void closeMemoryCache() {
        this.clearAllMemoryCache();
        this.memoryLruCache = null;
    }

    public void closeDiskCache() {
        synchronized(this.mDiskCacheLock) {
            if (this.diskLruCache != null) {
                try {
                    this.diskLruCache.close();
                    this.diskLruCache = null;
                } catch (IOException var4) {
                    var4.printStackTrace();
                }

            }
        }
    }

    public Bitmap getBitmapMemoryCache(String key) {
        return this.memoryLruCache != null ? (Bitmap)this.memoryLruCache.get(key) : null;
    }

    public void saveBitmapMemoryCache(Bitmap bitmap, String key) {
        if (this.memoryLruCache != null && key != null) {
            this.memoryLruCache.put(key, bitmap);
        }

    }

    public Bitmap getBitmapDiskCache(String key) {
        synchronized(this.mDiskCacheLock) {
            if (this.diskLruCache != null && key != null) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                DiskLruCache.Snapshot snapshot = null;

                Bitmap var10000;
                try {
                    snapshot = this.diskLruCache.get(key.hashCode() + "");
                    if (snapshot == null) {
                        return null;
                    }

                    options.inPreferredConfig = Bitmap.Config.RGB_565;
                    options.inInputShareable = true;
                    options.inPurgeable = true;
                    var10000 = BitmapFactory.decodeStream(snapshot.getInputStream(0), (Rect)null, options);
                } catch (OutOfMemoryError var9) {
                    LogUtil.e("getBitmapDiskCache:OutOfMemory");

                    try {
                        options.inSampleSize = 2;
                        var10000 = BitmapFactory.decodeStream(snapshot.getInputStream(0), (Rect)null, options);
                    } catch (Exception var8) {
                        var8.printStackTrace();
                        return null;
                    }

                    return var10000;
                } catch (Exception var10) {
                    var10.printStackTrace();
                    return null;
                }

                return var10000;
            } else {
                return null;
            }
        }
    }

    public void saveBitmapDiskCache(Bitmap bitmap, String key) {
        this.saveBitmapDiskCache(bitmap, key, Bitmap.CompressFormat.JPEG);
    }

    public void saveBitmapDiskCache(Bitmap bitmap, String key, Bitmap.CompressFormat format) {
        synchronized(this.mDiskCacheLock) {
            if (this.diskLruCache != null && key != null) {
                try {
                    DiskLruCache.Editor editor = this.diskLruCache.edit(key.hashCode() + "");
                    if (editor != null) {
                        OutputStream out = editor.newOutputStream(0);
                        bitmap.compress(format, 90, out);
                        editor.commit();
                        out.close();
                    }
                } catch (Exception var8) {
                    var8.printStackTrace();
                }

            }
        }
    }

    public Bitmap getBitmap(String key) {
        Bitmap bitmap = this.getBitmapMemoryCache(key);
        if (bitmap == null) {
            bitmap = this.getBitmapDiskCache(key);
            if (bitmap != null) {
                this.saveBitmapMemoryCache(bitmap, key);
            }
        }

        return bitmap;
    }

    public void save(Bitmap bitmap, String key, Bitmap.CompressFormat format) {
        this.saveBitmapMemoryCache(bitmap, key);
        this.saveBitmapDiskCache(bitmap, key, format);
    }

    public void save(Bitmap bitmap, String key) {
        this.saveBitmapMemoryCache(bitmap, key);
        this.saveBitmapDiskCache(bitmap, key);
    }

    public void flushDiskCache() {
        synchronized(this.mDiskCacheLock) {
            if (this.diskLruCache != null) {
                try {
                    this.diskLruCache.flush();
                } catch (Throwable var4) {
                    LogUtil.e(var4.getMessage());
                }
            }

        }
    }

    public int getMemoryCacheMaxSize() {
        return this.mMemoryCacheMaxSize;
    }

    public void setMemoryCacheMaxSize(int size) {
        if (this.memoryLruCache != null && Build.VERSION.SDK_INT >= 21) {
            this.memoryLruCache.resize(size);
        }

    }

    public long getDiskCacheMaxSize() {
        return this.mDiskCacheMaxSize;
    }

    public void setDiskCacheMaxSize(long size) {
        synchronized(this.mDiskCacheLock) {
            if (this.diskLruCache != null) {
                this.diskLruCache.setMaxSize(size);
            }
        }
    }

    public File getDiskCacheDir() {
        return this.mDiskCacheDir;
    }

    public Map<String, Bitmap> getSnapshotMemoryCache() {
        return this.memoryLruCache == null ? null : this.memoryLruCache.snapshot();
    }

    public static String getDiskCacheDir(Context context, String dirName) {
        String cachePath = null;
        File cacheDir;
        if ("mounted".equals(Environment.getExternalStorageState())) {
            cacheDir = context.getExternalCacheDir();
            if (cacheDir != null) {
                cachePath = cacheDir.getPath();
            }
        }

        if (cachePath == null) {
            cacheDir = context.getCacheDir();
            if (cacheDir != null && cacheDir.exists()) {
                cachePath = cacheDir.getPath();
            }
        }

        return cachePath + File.separator + dirName;
    }
}
