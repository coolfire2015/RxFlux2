package com.huyingbao.rxflux2.inject.module.application;

import com.huyingbao.rxflux2.RxFlux;
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
}
