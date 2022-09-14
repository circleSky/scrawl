package com.cennavi.doodle.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2016/9/3.
 */
public class StatusBarUtil {
    public StatusBarUtil() {
    }

    @SuppressLint("WrongConstant")
    public static void setStatusBarTranslucent(Window win, boolean translucent, boolean darkMode) {
        if (Build.VERSION.SDK_INT >= 19) {
            WindowManager.LayoutParams winParams = win.getAttributes();
            if (translucent) {
                winParams.flags |= WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            } else {
                winParams.flags &= -67108865;
            }

            win.setAttributes(winParams);
            if (!setStatusBarDarkModeMEIZU(win, darkMode)) {
                setStatusBarDarkModeXIAOMI(win, darkMode);
            }
        }

    }

    public static void setStatusBarTranslucent(Activity activity, boolean translucent, boolean darkMode) {
        setStatusBarTranslucent(activity.getWindow(), translucent, darkMode);
    }

    public static boolean setStatusBarDarkModeMEIZU(Window window, boolean dark) {
        try {
            WindowManager.LayoutParams lp = window.getAttributes();
            Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);
            int bit = darkFlag.getInt((Object)null);
            int value = meizuFlags.getInt(lp);
            if (dark) {
                value |= bit;
            } else {
                value &= ~bit;
            }

            meizuFlags.setInt(lp, value);
            window.setAttributes(lp);
            return true;
        } catch (Exception var7) {
            return false;
        }
    }

    public static boolean setStatusBarDarkModeXIAOMI(Window window, boolean darkmode) {
        Class clazz = window.getClass();

        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", Integer.TYPE, Integer.TYPE);
            extraFlagField.invoke(window, darkmode ? darkModeFlag : 0, darkModeFlag);
            return true;
        } catch (Exception var7) {
            return false;
        }
    }
}
