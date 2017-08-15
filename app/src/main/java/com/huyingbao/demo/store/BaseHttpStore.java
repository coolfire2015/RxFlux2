package com.huyingbao.demo.store;

import com.hardsoftstudio.rxflux.action.RxAction;
import com.hardsoftstudio.rxflux.dispatcher.Dispatcher;
import com.hardsoftstudio.rxflux.store.RxStoreChange;

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
