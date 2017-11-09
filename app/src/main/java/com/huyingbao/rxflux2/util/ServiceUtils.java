package com.huyingbao.rxflux2.util;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.huyingbao.rxflux2.constant.Constants;
import com.huyingbao.rxflux2.receiver.AlarmReceiver;

import java.util.ArrayList;

/**
 * Created by liujunfeng on 2017/1/1.
 */

public class ServiceUtils {
    /**
     * alarm 循环间隔,android 5.1以上默认最小间隔是60s
     */
    private static final long ALARM_INTERVAL = AlarmManager.INTERVAL_FIFTEEN_MINUTES;

    /**
     * 设置定时器,启动或者停止定时器,循环启动服务
     *
     * @param context
     * @param intent
     * @param isOn
     */
    public static void setAlarm(Context context, Intent intent, boolean isOn) {
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (isOn) {
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), ALARM_INTERVAL, pendingIntent);
        } else {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }

    /**
     * 判断定时器的启停状态
     * 一个 PendingIntent 只能登记一个定时器
     *
     * @param context
     * @param intent
     * @return
     */
    public static boolean isAlarmOn(Context context, Intent intent) {
        PendingIntent pi = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }

    /**
     * 启动服务
     * 启动定时器
     *
     * @param context
     */
    public static void startTimerCheck(Context context) {
        //启动服务
        startService(context);
        //每隔三分钟发一次广播
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(Constants.RECEIVER_NAME);
        //一个 PendingIntent 只能登记一个定时器
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        long firstTime = SystemClock.elapsedRealtime();//第一次启动时间
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, 60 * 1000, sender);
    }

    /**
     * 停止定时器
     * 停止服务
     *
     * @param context
     */
    public static void stopTimerCheck(Context context) {
        //停止服务
        stopService(context);
        //停止定时器
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(Constants.RECEIVER_NAME);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(sender);
    }

    /**
     * 判断服务是否运行
     *
     * @param context
     * @return
     */
    public static boolean isServiceRun(Context context, String serviceName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningServices = (ArrayList<ActivityManager.RunningServiceInfo>) activityManager.getRunningServices(300);
        for (int i = 0; i < runningServices.size(); i++) {
            if (runningServices.get(i).service.getClassName().toString().equals(serviceName))
                return true;
        }
        return false;
    }

    /**
     * 启动服务
     *
     * @param context
     */
    public static void startService(Context context) {
        Intent intentService = new Intent(Constants.SERVICE_NAME);
        intentService.setPackage(context.getPackageName());
        context.startService(intentService);
    }

    /**
     * 停止服务
     *
     * @param context
     */
    private static void stopService(Context context) {
        Intent intentService = new Intent(Constants.SERVICE_NAME);
        intentService.setPackage(context.getPackageName());
        context.stopService(intentService);
    }
}
