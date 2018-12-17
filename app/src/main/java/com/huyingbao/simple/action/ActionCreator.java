package com.huyingbao.simple.action;

import com.huyingbao.rxflux2.RxFlux;
import com.huyingbao.rxflux2.action.BaseRxActionCreator;
import com.huyingbao.rxflux2.action.RxAction;
import com.huyingbao.rxflux2.api.HttpApi;
import com.huyingbao.rxflux2.inject.scope.PerActivity;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * rxAction创建发送管理类
 * Created by liujunfeng on 2017/1/1.
 */
@PerActivity
public class ActionCreator extends BaseRxActionCreator implements Actions {
    @Inject
    HttpApi mHttpApi;

    @Inject
    ActionCreator(RxFlux rxFlux) {
        super(rxFlux.getDispatcher(), rxFlux.getDisposableManager());
    }

    @Override
    public void getProductList() {
        RxAction action = newRxAction(GET_GIT_REPO_LIST);
        postHttpAction(action, mHttpApi.getProductList().delay(5, TimeUnit.SECONDS));
    }
}
