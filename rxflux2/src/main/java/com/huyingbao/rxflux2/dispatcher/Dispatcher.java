package com.huyingbao.rxflux2.dispatcher;

import com.huyingbao.rxflux2.action.RxAction;
import com.huyingbao.rxflux2.store.RxStoreChange;

import org.greenrobot.eventbus.EventBus;

public class Dispatcher {

    private static Dispatcher sInstance;

    public static synchronized Dispatcher getInstance(RxBus rxBus) {
        if (sInstance == null) sInstance = new Dispatcher();
        return sInstance;
    }

    /**
     * 需要将store注册到dispatcher中
     *
     * @param rxStore
     * @param <T>     实现RxActionDispatch的RxStore
     */
    public <T extends RxActionDispatch> void subscribeRxStore(final T rxStore) {
        if (EventBus.getDefault().isRegistered(rxStore)) return;
        EventBus.getDefault().register(rxStore);
    }

    public <T extends RxViewDispatch> void subscribeRxView(final T rxView) {
        if (EventBus.getDefault().isRegistered(rxView)) return;
        EventBus.getDefault().register(rxView);
    }

    /**
     * 解除rxStore的注册
     *
     * @param object
     * @param <T>
     */
    public <T extends RxActionDispatch> void unsubscribeRxStore(final T object) {
        EventBus.getDefault().unregister(object);
    }

    /**
     * 将view解除注册
     *
     * @param object
     * @param <T>
     */
    public <T extends RxViewDispatch> void unsubscribeRxView(final T object) {
        EventBus.getDefault().unregister(object);
    }

    /**
     * 解除所有的注册
     */
    public synchronized void unsubscribeAll() {
        EventBus.getDefault().removeAllStickyEvents();
    }

    /**
     * 1:发送action变化,到所有订阅的store
     *
     * @param action
     */
    public void postRxAction(final RxAction action) {
        EventBus.getDefault().post(action);
    }

    /**
     * 2:发送store变化
     *
     * @param storeChange
     */
    public void postRxStoreChange(final RxStoreChange storeChange) {
        EventBus.getDefault().postSticky(storeChange);
    }
}
