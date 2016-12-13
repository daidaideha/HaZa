package com.lyl.haza.utils;

import android.content.Context;
import android.os.Environment;

import com.lyl.haza.utils.log.Log;

import java.io.File;

/**
 * Created by lyl on 2016/10/9.
 */
public class FileUtils {

    private static final String TAG = FileUtils.class.getSimpleName();

    /**
     * 获取 文件路径
     *
     * @param uniqueName 目录名
     * @return
     */
    public static File getDiskCacheDir(String uniqueName, Context context) {
        // getExternalCacheDir().getPath() = /storage/emulated/0/Android/data/com.huika.o2o.android.xmdd.debug/cache
        // Environment.getExternalStorageDirectory().getPath() = /storage/emulated/0
        // getFilesDir().getPath() = /data/data/com.huika.o2o.android.xmdd.debug/files
        // getCacheDir().getPath() = /data/data/com.huika.o2o.android.xmdd.debug/cache
        // 获取sd上的 Pictures Download DCIM 等目录
        // Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        // sd卡是否可用
        final boolean state = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
        final String cachePath = state && context.getExternalCacheDir() != null ?
                context.getExternalCacheDir().getAbsolutePath() :
                context.getCacheDir().getPath();
        File file = new File(cachePath + File.separator + uniqueName);
        if (!file.mkdirs()) {
            Log.i(TAG, uniqueName + "文件夹 已创建");
        }
        return file;
    }
}
