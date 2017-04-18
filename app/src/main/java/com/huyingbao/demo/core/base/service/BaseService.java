package com.huyingbao.demo.core.base.service;

import android.app.Service;
import android.content.Context;
import android.support.annotation.NonNull;

import com.hardsoftstudio.rxflux.RxFlux;
import com.hardsoftstudio.rxflux.action.RxAction;
import com.huyingbao.demo.core.actions.ActionCreator;
import com.huyingbao.demo.core.api.HttpApi;
import com.huyingbao.demo.core.inject.component.ApplicationComponent;
import com.huyingbao.demo.core.inject.component.DaggerServiceComponent;
import com.huyingbao.demo.core.inject.component.ServiceComponent;
import com.huyingbao.demo.core.inject.module.ServiceModule;
import com.huyingbao.demo.core.inject.qualifier.ContextLife;

import javax.inject.Inject;

/**
 * 所有服务的父类
 * Created by Liu Junfeng on 2017/1/1.
 */

public abstract class BaseService extends Service {
    @Inject
    @ContextLife("Service")
    protected Context mContext;

    @Inject
    protected RxFlux rxFlux;
    @Inject
    protected ActionCreator actionCreator;

    @Inject
    protected HttpApi HttpApi;

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
                .applicationComponent(ApplicationComponent.Instance.get())
                .build();
        //注入Injector
        initInjector();
    }

    /**
     * Service中快速创建action的方法
     *
     * @param actionId
     * @param data
     * @return
     */
    protected RxAction creatAction(@NonNull String actionId, @NonNull Object... data) {
        return actionCreator.newRxAction(actionId, data);
    }

    /**
     * 注入Injector
     */
    public abstract void initInjector();
}
