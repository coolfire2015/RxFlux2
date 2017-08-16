package com.huyingbao.demo.action;

import android.support.annotation.NonNull;

import com.huyingbao.rxflux2.action.RxAction;
import com.huyingbao.rxflux2.dispatcher.Dispatcher;
import com.huyingbao.rxflux2.util.DisposableManager;
import com.huyingbao.demo.api.HttpApi;
import com.huyingbao.demo.util.AppUtils;
import com.huyingbao.demo.util.LocalStorageUtils;

import javax.inject.Inject;

/**
 * action创建发送管理类
 * Created by liujunfeng on 2017/1/1.
 */
public class ActionCreator extends BaseRxActionCreator implements Actions {
    @Inject
    HttpApi mHttpApi;
    @Inject
    LocalStorageUtils mLocalStorageUtils;

    public ActionCreator(Dispatcher dispatcher, DisposableManager manager) {
        super(dispatcher, manager);
        AppUtils.getApplicationComponent().inject(this);
    }

    @Override
    public boolean retry(RxAction action) {
        return false;
    }

    @Override
    public void postBaseAction(@NonNull String actionId, @NonNull Object... data) {
        postRxAction(newRxAction(actionId, data));
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
