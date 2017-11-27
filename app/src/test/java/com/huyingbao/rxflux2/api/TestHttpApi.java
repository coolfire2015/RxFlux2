package com.huyingbao.rxflux2.api;

import android.content.Context;

import com.huyingbao.rxflux2.action.RxAction;
import com.huyingbao.rxflux2.constant.Actions;

/**
 * add:添加 get:获取 update:修改 delete:删除 apply:申请 confirm:确认 write:发送 check:检查
 * Created by liujunfeng on 2017/1/1.
 */
public class TestHttpApi implements Actions {


    @Override
    public boolean retry(RxAction action) {
        return false;
    }

    @Override
    public void getGitRepoList() {

    }

    @Override
    public void getGitUser(Context context, int userId) {

    }
}
