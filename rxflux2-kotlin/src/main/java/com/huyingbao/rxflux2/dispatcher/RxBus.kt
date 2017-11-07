package com.huyingbao.rxflux2.dispatcher

import io.reactivex.Flowable
import io.reactivex.processors.FlowableProcessor
import io.reactivex.processors.PublishProcessor

class RxBus private constructor() {
    private val mBus: FlowableProcessor<Any>

    init {
        mBus = PublishProcessor.create<Any>().toSerialized()
    }

    fun get(): FlowableProcessor<Any> {
        return mBus
    }

    fun send(o: Any) {
        mBus.onNext(o)
    }

    /**
     * 返回指定类型的带背压的Flowable实例
     *
     * @param tClass
     * @param <T>
     * @return
    </T> */
    fun <T> toFlowable(tClass: Class<T>): Flowable<T> {
        return mBus.ofType(tClass)
    }

    fun unregisterAll() {
        mBus.onComplete()
    }

    /**
     * 是否已有观察者订阅
     *
     * @return
     */
    fun hasSubscribers(): Boolean {
        return mBus.hasSubscribers()
    }

    companion object {
        private var sInstance: RxBus? = null

        val instance: RxBus
            @Synchronized get() {
                if (sInstance == null) sInstance = RxBus()
                return sInstance
            }
    }
}
