package com.huyingbao.rxflux2.action;

import android.support.annotation.NonNull;

import com.huyingbao.rxflux2.api.HttpApi;
import com.huyingbao.rxflux2.constant.Actions;
import com.huyingbao.rxflux2.dispatcher.Dispatcher;
import com.huyingbao.rxflux2.util.AppUtils;
import com.huyingbao.rxflux2.util.DisposableManager;

import javax.inject.Inject;

/**
 * rxAction创建发送管理类
 * Created by liujunfeng on 2017/1/1.
 */
public class ActionCreator extends BaseRxActionCreator implements Actions {
    @Inject
    HttpApi mHttpApi;

    public ActionCreator(Dispatcher dispatcher, DisposableManager manager) {
        super(dispatcher, manager);
        AppUtils.getApplicationComponent().inject(this);
    }

    @Override
    public boolean retry(RxAction action) {
        return false;
    }


    @Override
    public void getPublicRepositories() {
        RxAction action = newRxAction(GET_PUBLIC_REPOS);
        postHttpAction(action, mHttpApi.getRepositories());
    }

    @Override
    public void getUserDetails(String userId) {

    }
}
