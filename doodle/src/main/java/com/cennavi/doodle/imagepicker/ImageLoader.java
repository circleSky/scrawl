package com.cennavi.doodle.imagepicker;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

import com.cennavi.doodle.R;
import com.cennavi.doodle.image.ImageLoaderConfig;
import com.cennavi.doodle.image.ImageLoaderGroup;
import com.cennavi.doodle.image.LocalImagerLoader;

/**
 * Created by huangziwei on 16-9-1.
 */
public class ImageLoader {

    private static ImageLoader sInstance;

    private ImageLoaderGroup mImageLoaderGroup;

    public static ImageLoader getInstance(Context context) {
        if (sInstance == null) {
            synchronized (ImageLoader.class) {
                if (sInstance == null) {
                    sInstance = new ImageLoader(context);
                }
            }
        }
        return sInstance;
    }

    private ImageLoader(Context context) {
        context = context.getApplicationContext();
        int memoryCacheSize = (int) Runtime.getRuntime().maxMemory() / 8;

        mImageLoaderGroup = new ImageLoaderGroup(context, memoryCacheSize, 25 * 1024 * 1024);
        mImageLoaderGroup.addImageLoader(new LocalImagerLoader(context));
        ImageLoaderConfig config = mImageLoaderGroup.getImageLoaderConfig();
        config.setLoadingDrawable(context.getResources().getDrawable(R.drawable.doodle_btn_effect_panel));
        config.setLoadFailedDrawable(new ColorDrawable(Color.RED));
    }

    public void display(View view, String path) {
        mImageLoaderGroup.load(view, path);
    }


}
