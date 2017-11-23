package com.huyingbao.rxflux2.action;

import com.huyingbao.rxflux2.dispatcher.Dispatcher;
import com.huyingbao.rxflux2.util.SubscriptionManager;
import com.huyingbao.rxflux2.androidtest.inject.component.TestApplicationComponent;
import com.huyingbao.rxflux2.action.ActionCreatorImpl;
import com.huyingbao.rxflux2.utils.AppUtils;

/**
 * Created by liujunfeng on 2017/6/27.
 */

public class TestActionCreatorImpl extends ActionCreatorImpl {
    /**
     * 构造方法,传入dispatcher和订阅管理器
     */
    public TestActionCreatorImpl(Dispatcher dispatcher, SubscriptionManager manager) {
        super(dispatcher, manager);
        ((TestApplicationComponent) AppUtils.getApplicationComponent()).inject(this);
    }
}
