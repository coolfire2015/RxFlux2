package com.huyingbao.demo.utils;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.huyingbao.demo.receiver.AlarmReceiver;

import java.util.ArrayList;

/**
 * Created by Liu Junfeng on 2017/11/21.
 */

public class ServiceUtils {
    /**
     * 启动定时器检查,隔3分钟检测发送一次广播
     *
     * @param context
     */
    public static void startTimerCheck(Context context) {
        //Logger.e("启动定时器");
        Intent intent = new Intent(context, AlarmReceiver.class);
//        intent.setAction(ActionsKeys.ACTION_ALARM);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        long firstime = SystemClock.elapsedRealtime();//第一次启动时间
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstime, 3 * 60 * 1000, sender);
    }

    /**
     * 停止定时器
     *
     * @param context
     */
    public static void stopTimerCheck(Context context) {
        //Logger.e("停止定时器");
        Intent intent = new Intent(context, AlarmReceiver.class);
//        intent.setAction(ActionsKeys.ACTION_ALARM);
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
     * 停止服务
     *
     * @param context
     */
    public static void stopService(Context context) {
//        Intent intentService = new Intent(ActionsKeys.SERVICE_NETTY);
//        intentService.setPackage(context.getPackageName());
//        context.stopService(intentService);
    }

    /**
     * 启动服务
     *
     * @param context
     */
    public static void startService(Context context) {
//        Intent intentService = new Intent(ActionsKeys.SERVICE_NETTY);
//        intentService.setPackage(context.getPackageName());
//        context.startService(intentService);
    }
}
