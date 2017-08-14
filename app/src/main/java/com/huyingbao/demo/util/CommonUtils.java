package com.huyingbao.demo.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import java.util.List;

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
     * 判断list是否可用
     *
     * @param list
     * @return true:不为空且size>0
     */
    public static boolean isListAble(List list) {
        return list != null && list.size() > 0;
    }

    /**
     * 获取statusbar 的高度
     *
     * @param resources
     * @return
     */
    public static int getStatusBarHeight(Resources resources) {
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    /**
     * 当前activity是否显示在主界面
     *
     * @param context
     * @return true 当前可见
     */
    public static boolean isTopActivity(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
        if (tasksInfo.size() > 0) {
            // 应用程序位于堆栈的顶层
            if (context.getPackageName().equals(tasksInfo.get(0).topActivity.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否可见
     *
     * @param context
     * @return true 当前可见
     */
    public static boolean isVisible(Context context) {
        return !((KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE)).inKeyguardRestrictedInputMode();
    }

    /**
     * 设置textview值,如果没有值,则不显示
     *
     * @param textView
     * @param value
     */
    public static void setTextViewValue(@NonNull TextView textView, CharSequence value) {
        setTextViewValue(textView, value, "");
    }

    /**
     * 设置textview值,如果没有值,则不显示
     *
     * @param textView
     * @param value
     * @param title
     */
    public static void setTextViewValue(@NonNull TextView textView, CharSequence value, String title) {
        if (TextUtils.isEmpty(value)) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
            textView.setText(title + value);
        }
    }
}


