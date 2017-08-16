package com.huyingbao.demo.store;

import com.huyingbao.rxflux2.dispatcher.Dispatcher;
import com.huyingbao.rxflux2.store.RxStore;
import com.huyingbao.demo.inject.component.ApplicationComponent;
import com.huyingbao.demo.util.AppUtils;

/**
 * store 父类
 * Created by liujunfeng on 2016/12/20.
 */
public abstract class BaseRxStore extends RxStore {
    protected ApplicationComponent mApplicationComponent;

    public BaseRxStore(Dispatcher dispatcher) {
        super(dispatcher);
        mApplicationComponent = AppUtils.getApplicationComponent();
        initInjector();
    }

    /**
     * 注入Injector
     */
    public abstract void initInjector();
}
