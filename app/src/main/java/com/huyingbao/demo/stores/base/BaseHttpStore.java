package com.huyingbao.demo.stores.base;

import com.hardsoftstudio.rxflux.action.RxAction;
import com.hardsoftstudio.rxflux.dispatcher.Dispatcher;
import com.hardsoftstudio.rxflux.store.RxStore;
import com.hardsoftstudio.rxflux.store.RxStoreChange;
import com.huyingbao.demo.core.api.HttpApi;
import com.huyingbao.demo.core.inject.component.ApplicationComponent;
import com.huyingbao.demo.utils.LocalStorageUtils;

import javax.inject.Inject;

/**
 * 存在于 BaseApplication 的 mApplicationComponent 中 全局
 * Created by Liu Junfeng on 2017/1/1.
 */
public class BaseHttpStore extends RxStore {
    @Inject
    HttpApi mHttpApi;
    @Inject
    LocalStorageUtils mLocalStorageUtils;

    public static final String STORE_ID = "BaseHttpStore";

    public BaseHttpStore(Dispatcher dispatcher) {
        super(dispatcher);
        ApplicationComponent.Instance.get().inject(this);
    }

    @Override
    public void onRxAction(RxAction action) {
        switch (action.getType()) {
            case "default":
                break;
            default:
                return;
        }
        postChange(new RxStoreChange(STORE_ID, action));
    }
}
