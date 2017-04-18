package com.hardsoftstudio.rxflux.dispatcher;

import rx.Observable;
import rx.functions.Func0;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Rx version of an EventBus
 * 1、首先创建一个可同时充当Observer和Observable的Subject bus；
 * 2、在需要接收事件的地方，订阅该Subject（此时Subject是作为Observable），
 * 在这之后，一旦Subject接收到事件，立即发射给该订阅者；
 * 3、在我们需要发送事件的地方，将事件post至Subject，
 * 此时Subject作为Observer接收到事件（onNext），
 * 然后会发射给所有订阅该Subject的订阅者
 */
public class RxBus {

    private static RxBus instance;

    /**
     * Subject可以当作中间件来传递数据
     * PublishSubject只会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者
     * Subject同时充当了Observer和Observable的角色，Subject是非线程安全的，
     * 要避免该问题，需要将 Subject转换为一个 SerializedSubject ，
     * 上述RxBus类中把线程非安全的PublishSubject包装成线程安全的Subject
     */
    private final Subject<Object, Object> bus = new SerializedSubject<>(PublishSubject.create());

    private RxBus() {
    }

    /**
     * 单例RxBus
     * 首先创建一个可同时充当Observer和Observable的Subject bus；
     *
     * @return
     */
    public synchronized static RxBus getInstance() {
        if (instance == null) instance = new RxBus();
        return instance;
    }

    /**
     * 作为Observer接收到事件,发送提供了一个新的事件
     * 在我们需要发送事件的地方，将事件post至Subject，
     * 此时Subject作为Observer接收到事件（onNext），
     * 然后会发射给所有订阅该Subject的订阅者
     *
     * @param o 可以是RxAction或者RxStoreChange
     */
    public void send(Object o) {
        bus.onNext(o);
    }

    /**
     * 延迟发送推送
     *
     * @param o
     * @param subscriptionDelay
     */
    public void send(Object o, Func0<Observable<Object>> subscriptionDelay) {
        Observable.just(o)
                .delay(subscriptionDelay, o1 -> Observable.just(o1))
                .subscribe(o1 -> bus.onNext(o1));
    }

    /**
     * @return
     */
    public Observable<Object> get() {
        return bus;
    }

    public boolean hasObservers() {
        return bus.hasObservers();
    }
}
