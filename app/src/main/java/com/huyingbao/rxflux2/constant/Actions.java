package com.huyingbao.rxflux2.constant;

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
    //region 本地-action
    String NET_DISCONNECTED = "net_disconnected";
    String NET_CONNECTED = "net_connected";

    String TO_GIT_REPO_LIST = "to_git_repo_list";
    String TO_GIT_USER = "to_git_user";
    //endregion

    //region 网络-action
    String GET_GIT_REPO_LIST = "get_git_repo_list";
    String GET_GIT_USER = "get_git_user";
    //endregion

    //region 网络
    boolean retry(RxAction action);

    void getGitRepoList();

    void getGitUser(int userId);
    //endregion
}
