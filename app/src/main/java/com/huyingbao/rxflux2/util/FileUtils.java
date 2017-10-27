package com.huyingbao.rxflux2.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by liujunfeng on 2017/1/1.
 */
public class FileUtils {
    /**
     * Java文件操作 获取文件扩展名 Get the file extension, if no extension or file name
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return "";
    }

    /**
     * 返回带/的文件缓存路径
     *
     * @param context
     * @return
     */
    public static String getMsgFilePath(Context context) {
        return null;
//        return getCacheFilePath(context) + LocalStorageUtils.getInstance().getInt(ActionsKeys.USER_ID, ActionsKeys.DEFAULT_INT) + "/file/";
    }

    /**
     * 返回带/的图片缓存路径
     *
     * @param context
     * @return
     */
    public static String getImageFilePath(Context context) {
        return null;
//        return getCacheFilePath(context) + LocalStorageUtils.getInstance().getInt(ActionsKeys.USER_ID, ActionsKeys.DEFAULT_INT) + "/image/";
    }

    /**
     * 获取临时文件缓存路径
     *
     * @param context
     * @return
     */
    public static String getTempFilePath(Context context) {
        return getCacheFilePath(context) + "/temp/";
    }

    /**
     * 返回带/的图片缓存路径
     *
     * @param context
     * @return
     */
    public static String getCacheFilePath(Context context) {
        String path;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) { // SD卡可用
            path = Environment.getExternalStorageDirectory()
                    + File.separator
                    + context.getPackageName()
                    + File.separator + "cache" + File.separator;
        } else { // SD卡不可用
            path = context.getDir("cache", Context.MODE_PRIVATE).getAbsolutePath() + File.separator;
        }
        File file = new File(path);
        if (!file.exists())
            file.mkdir();
        return path;
    }
}
