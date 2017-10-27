package com.huyingbao.rxflux2.util.push;

import android.content.Context;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.huyingbao.rxflux2.util.DevUtils;


/**
 * 百度推送
 * Created by liujunfeng on 2017/1/1.
 */
public class BaiduPushBase {
    public static void start(Context context) {
        PushManager.startWork(context, PushConstants.LOGIN_TYPE_API_KEY, DevUtils.getMetaValue(context, "com.baidu.lbsapi.API_KEY"));
    }

    public static void stop(Context context) {
        PushManager.stopWork(context);
    }
}
