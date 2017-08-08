package com.huyingbao.demo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 开机启动广播接收器
 * Created by liujunfeng on 2017/1/1.
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        if (LocalStorageUtils.getInstance(context).getBoolean(ActionsKeys.IS_LOGIN, false)) {
//            //开启定时器
//            ServiceUtils.startTimerCheck(context);
//            //开启百度推送
//            BaiduPushBase.start(BaseApplication.getInstance());
//        }
    }
}
