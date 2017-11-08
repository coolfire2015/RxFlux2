package com.huyingbao.rxflux2.dispatcher;

import io.reactivex.Flowable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

public class RxBus {
    private static RxBus sInstance;
    private final FlowableProcessor<Object> mFlowableProcessor;

    private RxBus() {
        mFlowableProcessor = PublishProcessor.create().toSerialized();
    }

    public synchronized static RxBus getInstance() {
        if (sInstance == null) sInstance = new RxBus();
        return sInstance;
    }

    public FlowableProcessor<Object> get() {
        return mFlowableProcessor;
    }

    public void send(Object o) {
        mFlowableProcessor.onNext(o);
    }

    /**
     * 返回指定类型的带背压的Flowable实例
     *
     * @param tClass
     * @param <T>
     * @return
     */
    public <T> Flowable<T> toFlowable(Class<T> tClass) {
        return mFlowableProcessor.ofType(tClass);
    }

    public void unregisterAll() {
        mFlowableProcessor.onComplete();
    }

    /**
     * 是否已有观察者订阅
     *
     * @return
     */
    public boolean hasSubscribers() {
        return mFlowableProcessor.hasSubscribers();
    }
}
