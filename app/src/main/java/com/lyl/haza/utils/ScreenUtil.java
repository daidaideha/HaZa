package com.lyl.haza.utils;

import android.content.Context;
import android.graphics.Paint;
import android.util.DisplayMetrics;

import com.lyl.haza.HzZaApplication;

/**
 * Created by kongnan on 15-9-14.
 */
public class ScreenUtil {

    public static int screenWidth;
    public static int screenHeight;
    public static float density;
    public static float scaleDensity;
    public static float xdpi;
    public static float ydpi;
    public static int densityDpi;
    public static int screenMin;// 宽高中，最小的值

    public static void GetInfo(Context context) {
        if (null == context) {
            return;
        }
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        screenMin = (screenWidth > screenHeight) ? screenHeight : screenWidth;
        density = dm.density;
        scaleDensity = dm.scaledDensity;
        xdpi = dm.xdpi;
        ydpi = dm.ydpi;
        densityDpi = dm.densityDpi;
    }

    public static int dip2px(float dipValue) {
        final float scale = ScreenUtil.getDisplayDensity();
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(float pxValue) {
        final float scale = ScreenUtil.getDisplayDensity();
        return (int) (pxValue / scale + 0.5f);
    }

    private static float getDisplayDensity() {
        if (density == 0) {
            GetInfo(HzZaApplication.getInstance().getApplicationContext());
        }
        return density;
    }

    public static int getDisplayWidth() {
        if (screenWidth == 0) {
            GetInfo(HzZaApplication.getInstance().getApplicationContext());
        }
        return screenWidth;
    }

    public static int getDisplayHeight() {
        if (screenHeight == 0) {
            GetInfo(HzZaApplication.getInstance().getApplicationContext());
        }
        return screenHeight;
    }

    public static float getMeasureTextView(float fontSize, String str) {
        if ((fontSize < 0.0F) || (str == null) || (str.length() == 0)) {
            return 0.0F;
        }
        Paint localPaint = new Paint();
        localPaint.setTextSize(fontSize);
        return dip2px(localPaint.measureText(str));
    }
}
