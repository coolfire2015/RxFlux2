package com.hardsoftstudio.rxflux.dispatcher;

import android.support.v4.util.ArrayMap;

import com.hardsoftstudio.rxflux.action.RxAction;
import com.hardsoftstudio.rxflux.action.RxError;
import com.hardsoftstudio.rxflux.store.RxStoreChange;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;

/**
 * RxFlux dispatcher, contains the the registered actions, stores and the instance of the RxBus
 * responsible to send events to the stores. This class is used as a singleton.
 * Dispatcher不会被直接使用，而是通过通过一个ActionCreator来封装Dispatcher，并提供便捷的方法来分发View中产生的事件，
 * 消息的传递通过Action（Action是一个普通的POJO类）来封装。
 */
public class Dispatcher {

    private static Dispatcher instance;
    private final RxBus bus;
    private ArrayMap<String, Subscription> rxActionMap;
    private ArrayMap<String, Subscription> rxStoreMap;

    private Dispatcher(RxBus bus) {
        this.bus = bus;
        this.rxActionMap = new ArrayMap<>();
        this.rxStoreMap = new ArrayMap<>();
    }

    public static synchronized Dispatcher getInstance(RxBus rxBus) {
        if (instance == null) instance = new Dispatcher(rxBus);
        return instance;
    }

    /**
     * 需要将store注册到dispatcher中
     *
     * @param rxStore
     * @param <T>     实现RxActionDispatch的RxStore
     */
    public <T extends RxActionDispatch> void subscribeRxStore(final T rxStore) {
        //获取对象的类名
        final String rxStoreTag = rxStore.getClass().getSimpleName();
        //获取key(类名)对应的value(Subscription)
        Subscription subscription = rxActionMap.get(rxStoreTag);
        //如果订阅不为空或者订阅是取消状态,则进行订阅
        if (subscription == null || subscription.isUnsubscribed()) {
            //filter过滤,传入一个Func1类对象,参数Object,返回boolean,若是object是RxAction的子类实现,则返回true,执行订阅
            rxActionMap.put(rxStoreTag, bus.get()
                    .onBackpressureBuffer()
                    .filter(o -> o instanceof RxAction)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(o ->
                    {
                        //Post RxAction
                        //(RxStore extends RxActionDispatch)object调用onRxAction方法
                        rxStore.onRxAction((RxAction) o);
                    }));
        }
    }

    /**
     * 注册view 到错误监听
     *
     * @param rxView
     * @param <T>
     */
    public <T extends RxViewDispatch> void subscribeRxError(final T rxView) {
        final String rxViewErrorTag = rxView.getClass().getSimpleName() + "_error";
        Subscription subscription = rxActionMap.get(rxViewErrorTag);
        if (subscription == null || subscription.isUnsubscribed()) {
            rxActionMap.put(rxViewErrorTag, bus.get()
                    .onBackpressureBuffer()
                    .filter(o -> o instanceof RxError)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(o ->
                    {
                        rxView.onRxError((RxError) o);
                    }));
        }
    }

    /**
     * Bus(Subject被监听者)发送一个事件到所有订阅bus(Subject)的监听者Subscription
     * 当该事件是RxStoreChange的实现类的时候,
     * 调用监听者Subscription的方法回调方法call
     * 添加RxViewDispatch到dispatch的订阅中,
     * <p>
     * view 对应的类名 当作 key
     * bus 进行订阅,订阅事件当作 value
     * view 添加到rxStoreMap中之后,bus发送数据,在观察者中会回调rxViewDispatch的回调方法
     *
     * @param rxView
     * @param <T>
     */
    public <T extends RxViewDispatch> void subscribeRxView(final T rxView) {
        //获取传入的Object的名字
        final String rxViewTag = rxView.getClass().getSimpleName();
        //获取Map中Object名字对应的value 监听者
        Subscription subscription = rxStoreMap.get(rxViewTag);
        //如果监听者空或者没订阅被监听者,生成一个新的监听者,并将他添加到 storemap中
        if (subscription == null || subscription.isUnsubscribed()) {
            //获取rxbus实例,是一个Observable(被监听者)的子类对象
            //Subject=new SerializedSubject<>(PublishSubject.create())
            //会把在订阅(subscribe())发生的时间点之后来自原始Observable的数据发射给观察者
            rxStoreMap.put(rxViewTag, bus.get()
                    .onBackpressureBuffer()
                    .filter(o -> o instanceof RxStoreChange)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(o ->
                    {
                        //调用Activity,Fragment,View等所有实现了RxViewDispatch的类对象的onRxStoreChange方法
                        rxView.onRxStoreChanged((RxStoreChange) o);
                    }));
        }
        subscribeRxError(rxView);
    }

    /**
     * 判断是否注册
     *
     * @param object
     * @param <T>
     * @return {@code true} object对应的Subcription不为空且已经注册, {@code false} otherwise
     */
    public <T extends RxViewDispatch> boolean isSubscribeRxView(final T object) {
        //获取传入的Object的名字
        final String tag = object.getClass().getSimpleName();
        //获取Map中Object名字对应的value 监听者
        Subscription subscription = rxStoreMap.get(tag);
        return subscription != null && !subscription.isUnsubscribed();
    }

    /**
     * 解除rxstore的注册
     *
     * @param object
     * @param <T>
     */
    public <T extends RxActionDispatch> void unsubscribeRxStore(final T object) {
        String tag = object.getClass().getSimpleName();
        Subscription subscription = rxActionMap.get(tag);
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
            rxActionMap.remove(tag);
        }
    }

    /**
     * 解除错误注册
     *
     * @param object
     * @param <T>
     */
    public <T extends RxViewDispatch> void unsubscribeRxError(final T object) {
        String tag = object.getClass().getSimpleName() + "_error";
        Subscription subscription = rxActionMap.get(tag);
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
            rxActionMap.remove(tag);
        }
    }

    /**
     * 将view解除注册
     *
     * @param object
     * @param <T>
     */
    public <T extends RxViewDispatch> void unsubscribeRxView(final T object) {
        String tag = object.getClass().getSimpleName();
        Subscription subscription = rxStoreMap.get(tag);
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
            rxStoreMap.remove(tag);
        }
        unsubscribeRxError(object);
    }

    /**
     * 解除所有的注册
     */
    public synchronized void unsubscribeAll() {
        for (Subscription subscription : rxActionMap.values())
            subscription.unsubscribe();

        for (Subscription subscription : rxStoreMap.values())
            subscription.unsubscribe();

        rxActionMap.clear();
        rxStoreMap.clear();
    }

    /**
     * 1:发送action变化,到所有订阅的store
     *
     * @param action
     */
    public void postRxAction(final RxAction action) {
        bus.send(action);
    }

    /**
     * 1:发送action变化,到所有订阅的store,延迟
     *
     * @param action
     * @param subscriptionDelay
     */
    public void postRxAction(final RxAction action, Func0<Observable<Object>> subscriptionDelay) {
        bus.send(action, subscriptionDelay);
    }

    /**
     * 2:发送store变化
     *
     * @param storeChange
     */
    public void postRxStoreChange(final RxStoreChange storeChange) {
        bus.send(storeChange);
    }

    /**
     * 2:发送store变化,延迟
     *
     * @param storeChange
     * @param subscriptionDelay
     */
    public void postRxStoreChange(final RxStoreChange storeChange, Func0<Observable<Object>> subscriptionDelay) {
        bus.send(storeChange, subscriptionDelay);
    }
}
