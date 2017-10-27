package com.huyingbao.rxflux2.util;

import com.huyingbao.rxflux2.BuildConfig;
import com.huyingbao.rxflux2.constant.ActionsKeys;
import com.orhanobut.logger.Logger;

/**
 * 服务器工具类
 * Created by liujunfeng on 2017/1/1.
 */

public class ServerUtils {
    private static String DEBUG_SERVER_API = "http://160.6.80.151:1337";
    private static String SERVER_API = "https://api.github.com";

    /**
     * 获取api完整地址
     *
     * @return
     */
    public static String getServerApi() {
        return BuildConfig.DEBUG ? SERVER_API : SERVER_API;
    }

    public static void setServerState(boolean serverState) {
        //日志
        Logger.e("切换<" + (serverState ? "内" : "外") + ">服务器");
        //修改当前网络状态
        LocalStorageUtils.getInstance().setBoolean(ActionsKeys.SERVER_STATE, serverState);
        //修改api host
//        AppUtils.getApplicationComponent().getHostSelectionInterceptor().setHostUrl(DEBUG_SERVER_API);
    }

}
