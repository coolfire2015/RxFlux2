package com.huyingbao.demo.store;

import com.huyingbao.rxflux2.action.RxAction;
import com.huyingbao.rxflux2.dispatcher.Dispatcher;
import com.huyingbao.rxflux2.store.RxStoreChange;

/**
 * 存在于 BaseApplication 的 mApplicationComponent 中 全局
 * Created by liujunfeng on 2017/1/1.
 */
public class BaseHttpStore extends BaseRxStore {

    public BaseHttpStore(Dispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void initInjector() {
        mApplicationComponent.inject(this);
    }

    @Override
    public void onRxAction(RxAction action) {
        switch (action.getType()) {
            case "default":
                break;
            default:
                return;
        }
        postChange(new RxStoreChange(getClass().getSimpleName(), action));
    }
}
