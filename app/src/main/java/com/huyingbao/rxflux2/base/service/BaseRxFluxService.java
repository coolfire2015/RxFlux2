package com.huyingbao.rxflux2.base.service;

import android.app.Service;
import android.content.Context;
import android.support.annotation.NonNull;

import com.huyingbao.rxflux2.RxFlux;
import com.huyingbao.rxflux2.action.ActionCreator;
import com.huyingbao.rxflux2.action.RxError;
import com.huyingbao.rxflux2.api.HttpApi;
import com.huyingbao.rxflux2.dispatcher.RxViewDispatch;
import com.huyingbao.rxflux2.inject.component.DaggerServiceComponent;
import com.huyingbao.rxflux2.inject.component.ServiceComponent;
import com.huyingbao.rxflux2.inject.module.ServiceModule;
import com.huyingbao.rxflux2.inject.qualifier.ContextLife;
import com.huyingbao.rxflux2.util.AppUtils;

import javax.inject.Inject;

/**
 * 所有需要接收store的Fragment需要继承该类,实现RxFlux接口
 * Created by liujunfeng on 2017/1/1.
 */
public abstract class BaseRxFluxService extends Service implements RxViewDispatch {
    @Inject
    @ContextLife("Service")
    protected Context mContext;

    @Inject
    protected RxFlux mRxFlux;
    @Inject
    protected ActionCreator mActionCreator;

    @Inject
    protected HttpApi mHttpApi;

    protected ServiceComponent mServiceComponent;


    @Override
    public void onCreate() {
        //依赖注入
        inject();
        //注册view
        mRxFlux.getDispatcher().subscribeRxView(this);
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //解除view注册
        mRxFlux.getDispatcher().unsubscribeRxView(this);
    }

    @Override
    public void onRxError(@NonNull RxError error) {

    }

    /**
     * 依赖注入
     */
    protected void inject() {
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
