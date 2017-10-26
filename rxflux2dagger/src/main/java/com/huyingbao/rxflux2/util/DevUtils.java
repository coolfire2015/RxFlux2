package com.huyingbao.rxflux2.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

/**
 * Created by liujunfeng on 2017/1/1.
 */
public class DevUtils {
    /**
     * 获取ApiKey
     *
     * @param context
     * @param metaKey
     * @return
     */
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (PackageManager.NameNotFoundException e) {
        }
        return apiKey;
    }

    /**
     * apk是否安装
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isPkgInstalled(Context context, String packageName) {

        if (packageName == null || "".equals(packageName))
            return false;
        ApplicationInfo info;
        try {
            info = context.getPackageManager().getApplicationInfo(packageName, 0);
            return info != null;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * 获取应用版本名字
     *
     * @param context
     * @return
     */
    public static String getAppVersion(Context context) {
        String appVersion;
        try {
            appVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            appVersion = "1.0.0";
        }
        return appVersion;
    }
}
