package com.huyingbao.rxflux2.inject.module;

import android.app.Service;
import android.content.Context;

import com.huyingbao.rxflux2.inject.qualifier.ContextLife;
import com.huyingbao.rxflux2.inject.scope.PerService;

import dagger.Module;
import dagger.Provides;

/**
 * Module是一个依赖的制造工厂
 * Created by liujunfeng on 2017/1/1.
 */
@Module
public class ServiceModule {
    private Service mService;

    public ServiceModule(Service service) {
        mService = service;
    }

    @Provides
    @PerService
    @ContextLife("Service")
    public Context provideContext() {
        return mService;
    }
}
