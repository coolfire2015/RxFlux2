package com.huyingbao.demo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 系统时钟广播接收器
 * Created by liujunfeng on 2017/1/1.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
//            case ActionsKeys.ACTION_ALARM:
//                Logger.v("接收系统时钟广播");
//                if (LocalStorageUtils.getInstance().getBoolean(ActionsKeys.IS_LOGIN, false) && !ServiceUtils.isServiceRun(context, NettyService.class.getName()))
//                    ServiceUtils.startService(context);
//                break;
        }

    }
}
