package com.huyingbao.demo.core.actions;

/**
 * 本地action,用于fragment跳转等非网络操作
 * Created by Liu Junfeng on 2017/1/1.
 */
public interface BaseActions {
    String BASE_ACTION = "base_action";//同一个activity或者fragment中根据action的不同,进行不同的操作
    String GET_LOCATION = "get_location";
    String TO_LOADING_NEXT = "to_loading_next";//到首页引导页面的下一页
    String TO_REGISTER_USER = "to_register_user";//到注册用户页面
    String TO_REGISTER_SHOP = "to_register_shop";//到注册店铺页面
}
