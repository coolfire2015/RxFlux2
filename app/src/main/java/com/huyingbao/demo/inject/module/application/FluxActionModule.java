package com.huyingbao.demo.inject.module.application;

import com.hardsoftstudio.rxflux.RxFlux;
import com.hardsoftstudio.rxflux.util.LogLevel;
import com.huyingbao.demo.BuildConfig;
import com.huyingbao.demo.base.application.BaseApplication;
import com.huyingbao.demo.core.actions.ActionCreator;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Flux 依赖
 * Module是一个依赖的制造工厂
 * Created by Liu Junfeng on 2017/1/1.
 */
@Module
public class FluxActionModule {
    @Provides
    @Singleton
    public RxFlux provideRxFlux() {
        RxFlux.LOG_LEVEL = BuildConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE;
        return RxFlux.init(BaseApplication.getInstance());
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
