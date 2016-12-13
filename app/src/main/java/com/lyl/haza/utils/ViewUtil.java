package com.lyl.haza.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;

import com.lyl.haza.HzZaApplication;
import com.lyl.haza.utils.log.Log;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.lang.reflect.Method;
import java.util.List;

public class ViewUtil {

    private static final String TAG = ViewUtil.class.getSimpleName();

    public static String getImagePathWith140x100(String imageUrl) {
        return imageUrl + "?imageView2/1/w/140/h/100";
    }

    public static String getImagePathWith128x128(String imageUrl) {
        return imageUrl + "?imageView2/1/w/128/h/128";
    }

    public static String getImagePathWith1024x1024(String imageUrl) {
        return imageUrl + "?imageMogr2/thumbnail/1024x1024>";
    }

    /**
     * 为图片添加文字水印
     */
    public static void loadImageWithWatermark(Context context, String imageUrl, final ImageView imageView, final String waterMarkStr) {
        if (TextUtils.isEmpty(imageUrl) || imageView == null) {
            return;
        }
        if (imageUrl.contains("http")) {
            imageUrl = getImagePathWith1024x1024(imageUrl);
        }
        Picasso.with(context).load(imageUrl).transform(new Transformation() {
            @Override
            public Bitmap transform(Bitmap source) {
                int w = source.getWidth();
                int h = source.getHeight();
                float x = (float) Math.sqrt(Math.pow(w, 2) + Math.pow(h, 2)); // 斜边
                Bitmap newBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
                Canvas canvas = new Canvas(newBitmap);

                // @fulf 贝赛尔曲线 实现 水印 倾斜 效果
                Paint paint = new Paint();
                paint.setDither(true);
                paint.setFilterBitmap(true);
                canvas.drawBitmap(source, 0, 0, paint);

                // 释放 原图片 解析后的内存
                source.recycle();

                Paint textPaint = new Paint();// 设置画笔
                int lettersLen = waterMarkStr.length();
                textPaint.setTextSize(x * 0.9f / lettersLen); // 字体大小
                textPaint.setTypeface(Typeface.DEFAULT_BOLD);
                // 水印字体颜色
                textPaint.setColor(Color.parseColor("#3A000000"));
                textPaint.setTextAlign(Paint.Align.CENTER);

                int xPos = (canvas.getWidth() / 2);
                int yPos = (canvas.getHeight() / 2);

                float degrees = (float) (Math.atan((float) h / (float) w) / (Math.PI / 180));
                canvas.rotate(-degrees, xPos, yPos);
                yPos = (int) (yPos - ((textPaint.descent() + textPaint.ascent()) / 2)); // 文字baseline
                canvas.drawText(waterMarkStr, xPos, yPos, textPaint);
                canvas.save(Canvas.ALL_SAVE_FLAG);
                canvas.restore();
                return newBitmap;
            }

            @Override
            public String key() {
                return "key";
            }
        }).into(imageView);
    }

    /**
     * 为图片添加文字水印
     */
    public static void loadImageWithDateWatermark(Context context, String imageUrl, final ImageView imageView, final String waterMarkStr) {
        if (TextUtils.isEmpty(imageUrl) || imageView == null) {
            return;
        }
        if (imageUrl.contains("http")) {
            imageUrl = getImagePathWith1024x1024(imageUrl);
        }
        Picasso.with(context).load(imageUrl).transform(new Transformation() {
            @Override
            public Bitmap transform(Bitmap source) {
                return makeDateWatermark(source, waterMarkStr);
            }

            @Override
            public String key() {
                return "loadImageWithDateWatermark";
            }
        }).into(imageView);
    }

    public static Bitmap makeDateWatermark(Bitmap source, final String waterMarkStr) {
        int w = source.getWidth();
        int h = source.getHeight();
        Bitmap newBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(newBitmap);

        // @fulf 贝赛尔曲线 实现 水印 倾斜 效果
        Paint paint = new Paint();
        paint.setDither(true);
        paint.setFilterBitmap(true);
        canvas.drawBitmap(source, 0, 0, paint);

        // 释放 原图片 解析后的内存
        source.recycle();

        Paint textPaint = new Paint();// 设置画笔
        int lettersLen = waterMarkStr.length();
        textPaint.setTextSize(w * 0.6f / lettersLen); // 字体大小
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        // 水印字体颜色
        textPaint.setColor(Color.parseColor("#FFFFFFFF"));
        textPaint.setTextAlign(Paint.Align.CENTER);

        int paddingX = ScreenUtil.dip2px(10);
        int paddingY = ScreenUtil.dip2px(15);
        float textWidth = textPaint.measureText(waterMarkStr) / 2;
        float textHeight = (textPaint.descent() + textPaint.ascent()) / 2;
        float xPos = canvas.getWidth() - textWidth - paddingX;
        float yPos = canvas.getHeight() - textHeight - paddingY;

        canvas.drawText(waterMarkStr, xPos, yPos, textPaint);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return newBitmap;
    }

    public static Bitmap changeImageColor(Bitmap sourceBitmap, int color) {
        Bitmap resultBitmap = Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth() - 1, sourceBitmap.getHeight());
        Paint p = new Paint();
        ColorFilter filter = new LightingColorFilter(color, 1);
        p.setColorFilter(filter);

        Canvas canvas = new Canvas(resultBitmap);
        canvas.drawBitmap(resultBitmap, 0, 0, p);
        return resultBitmap;
    }

    public static Drawable covertBitmapToDrawable(Context context, Bitmap bitmap) {
        Drawable d = new BitmapDrawable(context.getResources(), bitmap);
        return d;
    }

    public static Bitmap convertDrawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    @SuppressWarnings("deprecation")
    public static void setBackground(final View view, final Drawable background) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackgroundDrawable(background);
        } else {
            ViewAccessorJB.setBackground(view, background);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    static class ViewAccessorJB {
        static void setBackground(final View view, final Drawable background) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) return;
            view.setBackground(background);
        }
    }

    public static void setGreyScale(View v, boolean greyScale) {
        if (greyScale) {
            // Create a paint object with 0 saturation (black and white)
            ColorMatrix cm = new ColorMatrix();
            cm.setSaturation(0);
            Paint greyScalePaint = new Paint();
            greyScalePaint.setColorFilter(new ColorMatrixColorFilter(cm));
            // Create a hardware layer with the grey scale paint
            v.setLayerType(View.LAYER_TYPE_HARDWARE, greyScalePaint);
        } else {
            // Remove the hardware layer
            v.setLayerType(View.LAYER_TYPE_NONE, null);
        }
    }

    /**
     * 设置小数位数控制
     *
     * @param decimalDigits 小数点位数
     * @return
     */
    public static InputFilter getDecimalInputFilter(final int decimalDigits) {
        return new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                // 删除等特殊字符，直接返回
                if ("".equals(source.toString())) {
                    return null;
                }
                String dValue = dest.toString();
                String[] splitArray = dValue.split("\\.");
                if (splitArray.length > 1) {
                    String dotValue = splitArray[1];
                    int diff = dotValue.length() + 1 - decimalDigits;
                    if (diff > 0 && source.length() > start && source.length() >= (end - diff)) {
                        return source.subSequence(start, end - diff);
                    }
                }
                return null;
            }
        };
    }

    /**
     * 禁止 粘贴和复制
     *
     * @param editText
     */
    public static void banCopyAndPaste(EditText editText) {
        editText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });
    }

    @SuppressLint("SetJavaScriptEnabled")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setWebViewSetting(WebView mWebView) {
        WebSettings webSettings = mWebView.getSettings();
        // 存在XSS漏洞
        webSettings.setJavaScriptEnabled(true);
        // 移除Android低版本XSS漏洞
        if (Build.VERSION.SDK_INT >= 11 && Build.VERSION.SDK_INT < 17) {
            try {
                mWebView.removeJavascriptInterface("searchBoxJavaBridge_");
                mWebView.removeJavascriptInterface("accessibility");
                mWebView.removeJavascriptInterface("accessibilityTraversal");
            } catch (Throwable tr) {
                tr.printStackTrace();
            }
        }
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setSupportZoom(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setGeolocationEnabled(true);
        webSettings.setDatabaseEnabled(true);
        if (VersionUtils.compareLessSDKVersion(VersionUtils.Android_SDK_4_2)) {
            webSettings.setAppCacheMaxSize(1024 * 1024 * 4);
            webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        }
        if (VersionUtils.compareSDKVersion(VersionUtils.Android_SDK_5_0)) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }
        mWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_UP:
                        if (!v.hasFocus()) {
                            v.requestFocus();
                        }
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 显示键盘
     *
     * @param view
     */
    public static void showIme(@NonNull View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService
                (Context.INPUT_METHOD_SERVICE);
        // the public methods don't seem to work for me, so… reflection.
        try {
            Method showSoftInputUnchecked = InputMethodManager.class.getMethod(
                    "showSoftInputUnchecked", int.class, ResultReceiver.class);
            showSoftInputUnchecked.setAccessible(true);
            showSoftInputUnchecked.invoke(imm, 0, null);
        } catch (Exception e) {
            // ho hum
        }
    }

    public static void hideIme(@NonNull View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context
                .INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 将View控件转换成Bitmap图片
     *
     * @param v
     * @return
     */
    public static Bitmap loadBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap(v.getLayoutParams().width, v.getLayoutParams().height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        if (v instanceof ViewPager) {
            // 因为ViewPager 是可以滚动的
            int width = v.getLayoutParams().width;
            c.translate(-width * ((ViewPager) v).getCurrentItem(), 0);
        }
        v.draw(c);
        return b;
    }

    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 600) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static boolean isFastDoubleClickForWebView() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 1500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static class NoLineClickSpan extends ClickableSpan {

        public NoLineClickSpan() {
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false); //去掉下划线
        }

        @Override
        public void onClick(View widget) {
        }
    }

    /**
     * 是否在前台
     *
     * @param context
     * @return
     */
    public static boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = context.getPackageName();
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null)
            return false;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.startsWith(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    public static String getDeviceId() {
        return Settings.Secure.getString(HzZaApplication.getInstance().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getDeviceModel() {
        String ret = Build.MANUFACTURER + " " + Build.MODEL;
        Log.d(TAG, "model: " + ret);
        return ret;
    }
}