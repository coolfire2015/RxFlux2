package com.huyingbao.demo.stores.base;

import android.support.annotation.NonNull;

import com.hardsoftstudio.rxflux.action.RxAction;
import com.hardsoftstudio.rxflux.dispatcher.Dispatcher;
import com.hardsoftstudio.rxflux.store.RxStore;
import com.hardsoftstudio.rxflux.store.RxStoreChange;
import com.huyingbao.demo.core.actions.BaseActions;
import com.huyingbao.demo.core.inject.component.ApplicationComponent;

/**
 * 存在于 BaseApplication 的 mApplicationComponent 中 全局
 * Created by Liu Junfeng on 2017/1/1.
 */
public class BaseStore extends RxStore implements BaseActions {
    /**
     * StoreId,用来在postChange(RxStoreChange change)时,生成RxStoreChange
     * 在接受RxStoreChange的时候,区分是哪个store
     */
    public static final String STORE_ID = "BaseStore";

    public BaseStore(Dispatcher dispatcher) {
        super(dispatcher);
        ApplicationComponent.Instance.get().inject(this);
    }

    /**
     * This callback will get all the actions, each store must react on the types he want and do
     * some logic with the model, for example add it to the list to cache it, modify
     * fields etc.. all the logic for the models should go here and then call postChange so the
     * view request the new data
     * 这个回调接收所有的actions(RxAction),每个store都必须根据action的type做出反应,,例如将其添加到列表缓存,修改字段等
     * 所有的逻辑模型应该在这里,然后调用postChange请求新数据视图
     */
    @Override
    public void onRxAction(RxAction action) {
        switch (action.getType()) {
            case TO_LOADING_NEXT:
            case TO_REGISTER_USER:
            case TO_REGISTER_SHOP:
                break;
            default://必须有,接收到非自己处理的action返回
                return;
        }
        postChange(new RxStoreChange(STORE_ID, action));//只发送自己处理的action
    }

    /**
     * store中原则上只是接收action,不生成action
     * 少数特例情况中可以生成action
     *
     * @param actionId
     * @param data
     * @return
     */
    public RxAction newRxAction(@NonNull String actionId, @NonNull Object... data) {
        if (actionId.isEmpty()) throw new IllegalArgumentException("Type must not be empty");

        if (data.length % 2 != 0)
            throw new IllegalArgumentException("Data must be a valid list of key,value pairs");

        RxAction.Builder actionBuilder = RxAction.type(actionId);
        int i = 0;
        while (i < data.length) {
            String key = (String) data[i++];
            Object value = data[i++];
            actionBuilder.bundle(key, value);
        }
        return actionBuilder.build();
    }
}
