package com.huyingbao.demo.base.service;

import android.app.Service;
import android.content.Context;

import com.huyingbao.demo.actions.ActionCreator;
import com.huyingbao.demo.api.HttpApi;
import com.huyingbao.demo.inject.component.DaggerServiceComponent;
import com.huyingbao.demo.inject.component.ServiceComponent;
import com.huyingbao.demo.inject.module.ServiceModule;
import com.huyingbao.demo.inject.qualifier.ContextLife;
import com.huyingbao.demo.stores.base.BaseStore;
import com.huyingbao.demo.util.AppUtils;
import com.huyingbao.demo.util.LocalStorageUtils;

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
    protected BaseStore mBaseStore;

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
