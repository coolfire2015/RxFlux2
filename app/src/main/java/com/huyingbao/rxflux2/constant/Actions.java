package com.huyingbao.rxflux2.constant;

import android.support.annotation.NonNull;

import com.huyingbao.rxflux2.action.RxAction;

/**
 * add:添加
 * get:获取
 * update:修改
 * delete:删除
 * apply:申请
 * confirm:确认
 * write:发送
 * check:检查
 * Created by liujunfeng on 2017/1/1.
 */
public interface Actions {
    //region 本地
    String NET_DISCONNECTED = "net_disconnected";
    String NET_CONNECTED = "net_connected";
    //endregion

    //region 网络
    String GET_PUBLIC_REPOS = "get_public_repos";
    String GET_USER = "get_user";
    //endregion

    //region 网络
    boolean retry(RxAction action);

    void postBaseAction(@NonNull String actionId, @NonNull Object... data);

    void getPublicRepositories();

    void getUserDetails(String userId);
    //endregion
}
