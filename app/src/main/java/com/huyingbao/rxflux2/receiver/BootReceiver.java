package com.huyingbao.rxflux2.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.baidu.android.pushservice.PushManager;
import com.huyingbao.rxflux2.constant.ActionsKeys;
import com.huyingbao.rxflux2.util.AppUtils;
import com.huyingbao.rxflux2.util.LocalStorageUtils;
import com.huyingbao.rxflux2.util.ServiceUtils;
import com.huyingbao.rxflux2.util.push.BaiduPushBase;

/**
 * 开机启动广播接收器
 * Created by liujunfeng on 2017/1/1.
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //开启定时器
        if (LocalStorageUtils.getInstance().getBoolean(ActionsKeys.IS_LOGIN, false))
            ServiceUtils.startTimerCheck(context);
        //开启百度推送
        if (!PushManager.isPushEnabled(context))
            BaiduPushBase.start(AppUtils.getApplication());
    }
}
