package com.huyingbao.rxflux2.action;

import com.huyingbao.rxflux2.dispatcher.Dispatcher;
import com.huyingbao.rxflux2.util.AppUtils;
import com.huyingbao.rxflux2.util.DisposableManager;
import com.huyingbao.simple.action.ActionCreator;

/**
 * Created by liujunfeng on 2017/6/27.
 */

public class TestActionCreator extends ActionCreator {
    /**
     * 构造方法,传入dispatcher和订阅管理器
     */
    public TestActionCreator(Dispatcher dispatcher, DisposableManager manager) {
        super(dispatcher, manager);
        AppUtils.getApplicationComponent().inject(this);
    }
}
