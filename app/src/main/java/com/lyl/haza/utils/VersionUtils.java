package com.lyl.haza.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

public class VersionUtils {
    public static final int Android_SDK_1_0 = 1;
    public static final int Android_SDK_1_1 = 2;
    public static final int Android_SDK_1_5 = 3;
    public static final int Android_SDK_1_6 = 4;
    public static final int Android_SDK_2_0 = 5;
    public static final int Android_SDK_2_0_1 = 6;
    public static final int Android_SDK_2_1 = 7;
    public static final int Android_SDK_2_2 = 8;
    public static final int Android_SDK_2_3_1 = 9;
    public static final int Android_SDK_2_3_3 = 10;
    public static final int Android_SDK_3_0 = 11;
    public static final int Android_SDK_3_1 = 12;
    public static final int Android_SDK_3_2 = 13;
    public static final int Android_SDK_4_0 = 14;
    public static final int Android_SDK_4_0_3 = 15;
    public static final int Android_SDK_4_1 = 16;
    public static final int Android_SDK_4_2 = 17;
    public static final int Android_SDK_4_3 = 18;
    public static final int Android_SDK_4_4 = 19;
    public static final int Android_SDK_5_0 = 21;
    public static final int Android_SDK_5_1 = 22;
    public static final int Android_SDK_6_0 = 23;

    // private static final String TAG = "VersionUtils";

    /**
     * Get current version number.
     * 
     * @return
     */
    public static String getApplicationVersionNumber(Context context) {
        String version = "?";
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            version = pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        }

        return version;
    }

    /**
     * Get application name.
     * 
     * @return
     */
    public static String getApplicationName(Context context) {
        String name = "?";
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            name = context.getString(pi.applicationInfo.labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return name;
    }

    public static int getSDKVersionNumber() {
        return Build.VERSION.SDK_INT;

    }

    static public String getOSVersionName() {
        return Build.VERSION.RELEASE;

    }

    // just for statistic , they defined 2.0.1 as 2.0Later

    // must to do ??
    public static String getOSVersionNameForStatis() {
        switch (getSDKVersionNumber()) {
            case Android_SDK_2_0_1:
                return "2.0Later";
            default:
                break;
        }

        return getOSVersionName();
    }

    public static boolean allowBindBlogByBrowser() {
        final int number = getSDKVersionNumber();

        return number < Android_SDK_2_2;
    }

    /**
     * 如果 当前版本 大于等于 目标版本 返回 ture
     * 
     * @param targetVersion
     *            目标版本
     * @return 当前版本 大于等于 目标版本
     */
    public static boolean compareSDKVersion(int targetVersion) {
        final int number = getSDKVersionNumber();

        return number >= targetVersion;
    }

    /**
     * 如果 当前版本 小于等于 目标版本 返回 ture
     * 
     * @param targetVersion
     *            目标版本
     * @return 当前版本 大于等于 目标版本
     */
    public static boolean compareLessSDKVersion(int targetVersion) {
        final int number = getSDKVersionNumber();

        return number <= targetVersion;
    }

    public static boolean hasHoneycomb() {

        return compareSDKVersion(Android_SDK_3_0);
    }
}
