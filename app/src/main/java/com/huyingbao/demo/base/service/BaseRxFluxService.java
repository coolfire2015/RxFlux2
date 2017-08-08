package com.huyingbao.demo.base.service;

import android.app.Service;
import android.content.Context;
import android.support.annotation.NonNull;

import com.hardsoftstudio.rxflux.RxFlux;
import com.hardsoftstudio.rxflux.action.RxAction;
import com.hardsoftstudio.rxflux.action.RxError;
import com.hardsoftstudio.rxflux.dispatcher.RxViewDispatch;
import com.huyingbao.demo.core.actions.ActionCreator;
import com.huyingbao.demo.core.api.HttpApi;
import com.huyingbao.demo.core.inject.component.DaggerServiceComponent;
import com.huyingbao.demo.inject.component.ApplicationComponent;
import com.huyingbao.demo.inject.component.ServiceComponent;
import com.huyingbao.demo.inject.module.ServiceModule;
import com.huyingbao.demo.inject.qualifier.ContextLife;

import javax.inject.Inject;

/**
 * TODO 暂未用到
 * 所有需要接收store的Fragment需要继承该类,实现RxFlux接口
 * Created by liujunfeng on 2017/1/1.
 */

public abstract class BaseRxFluxService extends Service implements RxViewDispatch {
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
        //注册view
        rxFlux.getDispatcher().subscribeRxView(this);
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //解除view注册
        rxFlux.getDispatcher().unsubscribeRxView(this);
    }

    @Override
    public void onRxError(@NonNull RxError error) {

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
     * 依赖注入
     */
    protected void inject() {
        //初始化注入器
        mServiceComponent = DaggerServiceComponent.builder()
                .serviceModule(new ServiceModule(this))
                .applicationComponent(ApplicationComponent.Instance.get())
                .build();
        //注入Injector
        initInjector();
    }

    /**
     * 注入Injector
     */
    public abstract void initInjector();
}
