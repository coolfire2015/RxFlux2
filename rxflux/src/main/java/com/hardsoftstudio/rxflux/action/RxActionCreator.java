package com.hardsoftstudio.rxflux.action;

import android.support.annotation.NonNull;

import com.hardsoftstudio.rxflux.dispatcher.Dispatcher;
import com.hardsoftstudio.rxflux.util.SubscriptionManager;
import com.orhanobut.logger.Logger;

import rx.Observable;
import rx.Subscription;
import rx.functions.Func0;

/**
 * This class must be extended in order to give useful functionality to create RxAction.
 * 这个类必须被继承,提供一个可以创建RxAction的方法.
 * 按钮被点击触发回调方法，在回调方法中调用ActionCreator提供的有语义的的方法，ActionCreator会根据传入参数创建Action并通过Dispatcher发送给Store，
 * 所有订阅了这个Action的Store会接收到订阅的Action并消化Action，
 * 然后Store会发送UI状态改变的事件给相关的Activity（或Fragment)，
 * Activity在收到状态发生改变的事件之后，开始更新UI（更新UI的过程中会从Store获取所有需要的数据）。
 */
public abstract class RxActionCreator {

    private final Dispatcher dispatcher;
    /**
     * rxjava 观察者的管理者
     */
    private final SubscriptionManager manager;

    /**
     * 构造方法,传入dispatcher和订阅管理器
     *
     * @param dispatcher
     * @param manager
     */
    public RxActionCreator(Dispatcher dispatcher, SubscriptionManager manager) {
        this.dispatcher = dispatcher;
        this.manager = manager;
    }

    /**
     * 主要是为了和rxjava整合,用在调用网络接口api获取数据之后,被观察者得到数据,发生订阅关系,将返回的数据
     * 或者error封装成action,postAction或者postError出去
     * 订阅管理,将Rxaction和Subscription添加到SubscriptionManager
     *
     * @param rxAction
     * @param subscription
     */
    protected void addRxAction(RxAction rxAction, Subscription subscription) {
        manager.add(rxAction, subscription);
    }

    /**
     * 订阅管理器是否已经有了该action
     *
     * @param rxAction
     * @return
     */
    protected boolean hasRxAction(RxAction rxAction) {
        return manager.contains(rxAction);
    }

    /**
     * 订阅管理器,移除该action
     *
     * @param rxAction
     */
    private void removeRxAction(RxAction rxAction) {
        manager.remove(rxAction);
    }

    /**
     * 创建新的RxAction
     *
     * @param actionId -action type对应具体是什么样的方法
     * @param data     -键值对型的参数pair-value parameters(Key - Object))
     * @return
     */
    protected RxAction newRxAction(@NonNull String actionId, @NonNull Object... data) {
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

    /**
     * 通过调度器dispatcher将action推出去
     *
     * @param action
     */
    protected void postRxAction(@NonNull RxAction action) {
        dispatcher.postRxAction(action);
        removeRxAction(action);
    }

    /**
     * 有条件通过调度器dispatcher将action推出去
     *
     * @param action
     */
    protected void postRxAction(@NonNull RxAction action, Func0<Observable<Object>> subscriptionDelay) {
        dispatcher.postRxAction(action, subscriptionDelay);
        removeRxAction(action);
    }

    /**
     * 通过调度器dispatcher将error action推出去
     *
     * @param action
     * @param throwable
     */
    protected void postError(@NonNull RxAction action, Throwable throwable) {
        Logger.e("==错误:" + action.getType() + (throwable == null ? "" : "\n==错误信息:" + throwable.toString()));
        dispatcher.postRxAction(RxError.newRxError(action, throwable));
        removeRxAction(action);
    }
}
