package com.huyingbao.demo.utils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;

import java.util.Map;

/**
 * Created by liujunfeng on 2017/1/1.
 */
public class CommonUtils {

    /**
     * 得到用时间戳生成的文件名字
     *
     * @param localPath
     * @return
     */
    public static String getFileNameByTime(String localPath) {
        return System.currentTimeMillis() + "." + FileUtils.getExtensionName(localPath);
    }

    /**
     * 隐藏输入法
     */
    public static void closeInput(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && inputMethodManager.isActive()) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 网络接口请求中的限制参数封装方法
     *
     * @param skip
     * @param limit
     * @param sort
     * @return
     */
    public static Map<String, String> getOption(int skip, int limit, String sort) {
        Map<String, String> options = new ArrayMap<>();
        options.put("skip", skip + "");
        options.put("limit", limit + "");
        if (!TextUtils.isEmpty(sort)) options.put("sort", sort);
        return options;
    }

    /**
     * 网络接口请求中的限制参数封装方法
     *
     * @param data
     * @return
     */
    public static Map<String, Object> getQueryMap(@NonNull Object... data) {
        if (data.length % 2 != 0)
            throw new IllegalArgumentException("Data must be a valid list of key,value pairs");
        ArrayMap<String, Object> queryMap = new ArrayMap<>();
        int i = 0;
        while (i < data.length) queryMap.put((String) data[i++], data[i++]);
        return queryMap;
    }
}


