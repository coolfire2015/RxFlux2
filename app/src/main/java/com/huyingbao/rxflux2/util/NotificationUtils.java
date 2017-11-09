package com.huyingbao.rxflux2.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import com.huyingbao.simple.R;

/**
 * Created by liujunfeng on 2017/1/1.
 */
public class NotificationUtils {

    /**
     * 聊天推送消息显示提醒
     *
     * @param context
     * @param title
     * @param content
     * @param clazz
     */
    public static void showNotification(Context context, String title, String content, Class<?> clazz) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, title);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setTicker(title);// 第一次提示消息的时候显示在通知栏上
        builder.setAutoCancel(true);// 自己维护通知的消失
        builder.setDefaults(Notification.DEFAULT_ALL);

        // 构建一个Intent
        Intent resultIntent = new Intent(context, clazz);
        // 封装一个Intent
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // 设置通知主题的意图
        builder.setContentIntent(resultPendingIntent);
        // 获取通知管理器对象
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(title.hashCode(), builder.build());
    }

    /**
     * 清除聊天推送消息
     *
     * @param context
     * @param unique
     */
    public static void clearNotification(Context context, @NonNull String unique) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(unique.hashCode());
    }
}
