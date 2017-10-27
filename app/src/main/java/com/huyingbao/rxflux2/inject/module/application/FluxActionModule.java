package com.huyingbao.rxflux2.inject.module.application;

import com.huyingbao.rxflux2.RxFlux;
import com.huyingbao.rxflux2.action.ActionCreator;
import com.huyingbao.rxflux2.util.AppUtils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Flux 依赖
 * Module是一个依赖的制造工厂
 * Created by liujunfeng on 2017/1/1.
 */
@Module
public class FluxActionModule {
    @Provides
    @Singleton
    public RxFlux provideRxFlux() {
        return RxFlux.init(AppUtils.getApplication());
    }

    /**
     * @param rxFlux 从当前module中调用返回值是RxFlux的方法
     * @return
     */
    @Provides
    @Singleton
    public ActionCreator provideActionCreator(RxFlux rxFlux) {
        return new ActionCreator(rxFlux.getDispatcher(), rxFlux.getSubscriptionManager());
    }
}
