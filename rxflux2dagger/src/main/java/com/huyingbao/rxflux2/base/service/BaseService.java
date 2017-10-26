package com.huyingbao.rxflux2.base.service;

import android.app.Service;
import android.content.Context;

import com.huyingbao.rxflux2.action.ActionCreator;
import com.huyingbao.rxflux2.api.HttpApi;
import com.huyingbao.rxflux2.inject.component.DaggerServiceComponent;
import com.huyingbao.rxflux2.inject.component.ServiceComponent;
import com.huyingbao.rxflux2.inject.module.ServiceModule;
import com.huyingbao.rxflux2.inject.qualifier.ContextLife;
import com.huyingbao.rxflux2.store.AppStore;
import com.huyingbao.rxflux2.util.AppUtils;
import com.huyingbao.rxflux2.util.LocalStorageUtils;

import javax.inject.Inject;

/**
 * 所有服务的父类
 * Created by liujunfeng on 2017/1/1.
 */

public abstract class BaseService extends Service {
    @Inject
    @ContextLife("Service")
    protected Context mContext;

    @Inject
    protected AppStore mAppStore;

    @Inject
    protected ActionCreator mActionCreator;

    @Inject
    protected LocalStorageUtils mLocalStorageUtils;

    @Inject
    protected HttpApi mHttpApi;

    protected ServiceComponent mServiceComponent;

    @Override
    public void onCreate() {
        //依赖注入
        inject();
        super.onCreate();
    }

    private void inject() {
        //初始化注入器
        mServiceComponent = DaggerServiceComponent.builder()
                .serviceModule(new ServiceModule(this))
                .applicationComponent(AppUtils.getApplicationComponent())
                .build();
        //注入Injector
        initInjector();
    }

    /**
     * 注入Injector
     */
    public abstract void initInjector();
}
