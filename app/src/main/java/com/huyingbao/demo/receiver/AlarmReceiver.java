package com.huyingbao.demo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.baidu.android.pushservice.PushManager;
import com.huyingbao.demo.base.service.BaseRxFluxService;
import com.huyingbao.demo.constant.ActionsKeys;
import com.huyingbao.demo.constant.Constants;
import com.huyingbao.demo.util.AppUtils;
import com.huyingbao.demo.util.LocalStorageUtils;
import com.huyingbao.demo.util.ServiceUtils;
import com.huyingbao.demo.util.push.BaiduPushBase;
import com.orhanobut.logger.Logger;

/**
 * 系统时钟广播接收器
 * Created by liujunfeng on 2017/1/1.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case Constants.RECEIVER_NAME:
                Logger.v("接收系统时钟广播");
                if (LocalStorageUtils.getInstance().getBoolean(ActionsKeys.IS_LOGIN, false) && !ServiceUtils.isServiceRun(context, BaseRxFluxService.class.getName()))
                    ServiceUtils.startService(context);
                if (!PushManager.isPushEnabled(context))
                    BaiduPushBase.start(AppUtils.getApplication());
                break;
        }
    }
}
