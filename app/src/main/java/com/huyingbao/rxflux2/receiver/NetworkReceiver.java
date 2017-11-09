package com.huyingbao.rxflux2.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Parcelable;

import com.huyingbao.rxflux2.constant.Actions;
import com.huyingbao.rxflux2.util.AppUtils;
import com.orhanobut.logger.Logger;

import static android.net.wifi.WifiManager.WIFI_STATE_DISABLED;
import static android.net.wifi.WifiManager.WIFI_STATE_DISABLING;
import static android.net.wifi.WifiManager.WIFI_STATE_ENABLED;
import static android.net.wifi.WifiManager.WIFI_STATE_ENABLING;
import static android.net.wifi.WifiManager.WIFI_STATE_UNKNOWN;

/**
 * 网络连接改变监听器
 * Created by liujunfeng on 2017/1/1.
 */

public class NetworkReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //监听wifi的开启关闭状态
        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
            switch (wifiState) {
                case WIFI_STATE_DISABLED:
                    Logger.v("wifiState:不可用");
                    break;
                case WIFI_STATE_DISABLING:
                    Logger.v("wifiState:正在关闭");
                    break;
                case WIFI_STATE_ENABLED:
                    Logger.v("wifiState:可用");
                    break;
                case WIFI_STATE_ENABLING:
                    Logger.v("wifiState:正在开启");
                    break;
                case WIFI_STATE_UNKNOWN:
                    Logger.v("wifiState:未知");
                    break;
            }
        }
        // 监听wifi的连接状态,即是否连上了一个有效无线路由
        if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
            Parcelable parcelableExtra = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (null != parcelableExtra) {
                // 获取联网状态的NetWorkInfo对象
                NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                //获取的State对象则代表着连接成功与否等状态
                NetworkInfo.State state = networkInfo.getState();
                //判断网络是否已经连接
                boolean isConnected = state == NetworkInfo.State.CONNECTED;
                if (isConnected) {
                    Logger.v("连接到路由器");
                } else {
                    Logger.v("未连接路由器");
                }
            }
        }
        // 监听网络连接，包括wifi和移动数据的打开和关闭,以及连接上可用的连接都会接到监听
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            //获取联网状态的NetworkInfo对象
            NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (info == null) return;
            //如果当前的网络连接成功并且网络连接可用
            if (NetworkInfo.State.CONNECTED == info.getState() && info.isAvailable()) {
                if (info.getType() == ConnectivityManager.TYPE_WIFI || info.getType() == ConnectivityManager.TYPE_MOBILE) {
                    Logger.v(getConnectionType(info.getType()) + "连上");
                    AppUtils.getApplicationComponent().getActionCreator().postLocalAction(Actions.NET_CONNECTED);
                }
            } else {
                Logger.v(getConnectionType(info.getType()) + "断开");
                AppUtils.getApplicationComponent().getActionCreator().postLocalAction(Actions.NET_DISCONNECTED);
            }
        }
    }

    /**
     * 获取网络类型
     *
     * @param type
     * @return
     */
    private String getConnectionType(int type) {
        String connType = "";
        if (type == ConnectivityManager.TYPE_MOBILE) {
            connType = "手机网络数据";
        } else if (type == ConnectivityManager.TYPE_WIFI) {
            connType = "WIFI网络";
        }
        return connType;
    }
}
