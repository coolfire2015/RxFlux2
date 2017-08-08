package com.huyingbao.demo.actions;

import com.hardsoftstudio.rxflux.action.RxAction;

/**
 * 本地action,用于fragment跳转等非网络操作
 * Created by liujunfeng on 2017/1/1.
 */
public interface Actions {
    String GET_PUBLIC_REPOS = "get_public_repos";

    void getPublicRepositories();

    String GET_USER = "get_user";

    void getUserDetails(String userId);

    boolean retry(RxAction action);
}
