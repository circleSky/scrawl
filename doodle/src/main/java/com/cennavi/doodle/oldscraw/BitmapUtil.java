package com.cennavi.doodle.oldscraw;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Author:wuzhuo
 * Date:2017/4/11 10:40
 */

public class BitmapUtil {

    /**
     *
     * 图片过大则根据容器把原始图片改变大小。从而适应容器。
     * 否则改变画板大小适应图片
     *
     * @param bitmap
     * @param boardView
     * @return
     */
    public static Bitmap resizeBitmap(Bitmap bitmap, View boardView) {

        int bitmapHeight = bitmap.getHeight();
        int bitmapWidth = bitmap.getWidth();
        int boardHeight = boardView.getHeight();
        int boardWidth = boardView.getWidth();

        float scale = 1f;
        if(bitmapHeight > boardHeight || bitmapWidth > boardWidth){
            scale = bitmapHeight > bitmapWidth
                    ? boardHeight / (bitmapHeight * 1f)
                    : boardWidth / (bitmapWidth * 1f);
        }

        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, false);

        ViewGroup.LayoutParams params = boardView.getLayoutParams();
        params.height = resizeBitmap.getHeight();
        params.width = resizeBitmap.getWidth();
        boardView .setLayoutParams(params);

        return resizeBitmap;
    }


    /**
     * 截屏并将图片保存到相应路径下
     *
     * @param activity 当前需要截屏的activity
     * @param path     图片保存路径
     */
    public static void SaveScreenShot(Activity activity, String path) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            view.setDrawingCacheEnabled(false);
        }
    }
    /*
     * 保存文件，文件名为当前日期
     */
    public static boolean saveBitmap(Activity activity, Bitmap bitmap, String bitName) {
        String fileName;
        File file;
        if (Build.BRAND.equals("xiaomi")) { // 小米手机
            fileName = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/" + bitName;
        }else if (Build.BRAND.equals("Huawei")){
            fileName = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/" + bitName;
        } else {  // Meizu 、Oppo
            fileName = Environment.getExternalStorageDirectory().getPath() + "/DCIM/" + bitName;
        }
        file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        Log.e("文件位置",""+fileName);
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            // 格式为 JPEG，照相机拍出的图片为JPEG格式的，PNG格式的不能显示在相册中
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)) {
                out.flush();
                out.close();
                // 插入图库
                MediaStore.Images.Media.insertImage(activity.getContentResolver(), file.getAbsolutePath(), bitName, null);
                return true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        // 发送广播，通知刷新图库的显示
        // 加这句  在相册中会产生两张一样的图片
//        this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + fileName)));
        return false;
    }
}
