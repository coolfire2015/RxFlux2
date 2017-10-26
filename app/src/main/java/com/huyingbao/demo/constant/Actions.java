package com.huyingbao.demo.constant;

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
    String BASE_ACTION = "base_action";//同一个activity或者fragment中根据action的不同,进行不同的操作
    String GET_LOCATION = "get_location";
    String TO_LOADING_NEXT = "to_loading_next";//到首页引导页面的下一页
    String TO_REGISTER_USER = "to_register_user";//到注册用户页面
    String TO_REGISTER_SHOP = "to_register_shop";//到注册店铺页面
    String NET_DISCONNECTED = "net_disconnected";
    String NET_CONNECTED = "net_connected";

    //endregion
    boolean retry(RxAction action);

    void postBaseAction(@NonNull String actionId, @NonNull Object... data);

    String GET_PUBLIC_REPOS = "get_public_repos";

    void getPublicRepositories();

    String GET_USER = "get_user";

    void getUserDetails(String userId);

}
