package com.huyingbao.rxflux2.dispatcher

import android.support.v4.util.ArrayMap

import com.huyingbao.rxflux2.action.RxAction
import com.huyingbao.rxflux2.action.RxError
import com.huyingbao.rxflux2.store.RxStoreChange

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

/**
 * RxFlux dispatcher, contains the the registered actions, stores and the sInstance of the RxBus
 * responsible to send events to the stores. This class is used as a singleton.
 * Dispatcher不会被直接使用，而是通过通过一个ActionCreator来封装Dispatcher，并提供便捷的方法来分发View中产生的事件，
 * 消息的传递通过Action（Action是一个普通的POJO类）来封装。
 */
class Dispatcher private constructor(private val mRxBus: RxBus) {
    private val mRxActionMap: ArrayMap<String, Disposable>
    private val mRxStoreMap: ArrayMap<String, Disposable>

    init {
        this.mRxActionMap = ArrayMap()
        this.mRxStoreMap = ArrayMap()
    }

    /**
     * 需要将store注册到dispatcher中
     *
     * @param rxStore
     * @param <T>     实现RxActionDispatch的RxStore
    </T> */
    fun <T : RxActionDispatch> subscribeRxStore(rxStore: T) {
        //获取对象的类名
        val rxStoreTag = rxStore.javaClass.simpleName
        //获取key(类名)对应的value(Subscription)
        val disposable = mRxActionMap[rxStoreTag]
        //如果订阅不为空或者订阅是取消状态,则进行订阅
        if (disposable == null || disposable.isDisposed) {
            //filter过滤,传入一个Func1类对象,参数Object,返回boolean,若是object是RxAction的子类实现,则返回true,执行订阅
            mRxActionMap.put(rxStoreTag, mRxBus.get()
                    .onBackpressureBuffer()
                    .filter { o -> o is RxAction }
                    .observeOn(AndroidSchedulers.mainThread())
                    //Post RxAction (RxStore extends RxActionDispatch)object调用onRxAction方法
                    .subscribe { o -> rxStore.onRxAction(o as RxAction) })
        }
    }

    /**
     * 注册view 到错误监听
     *
     * @param rxView
     * @param <T>
    </T> */
    fun <T : RxViewDispatch> subscribeRxError(rxView: T) {
        val rxViewErrorTag = rxView.javaClass.simpleName + "_error"
        val disposable = mRxActionMap[rxViewErrorTag]
        if (disposable == null || disposable.isDisposed) {
            mRxActionMap.put(rxViewErrorTag, mRxBus.get()
                    .onBackpressureBuffer()
                    .filter { o -> o is RxError }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { o -> rxView.onRxError(o as RxError) })
        }
    }

    /**
     * Bus(Subject被监听者)发送一个事件到所有订阅bus(Subject)的监听者Subscription
     * 当该事件是RxStoreChange的实现类的时候,
     * 调用监听者Subscription的方法回调方法call
     * 添加RxViewDispatch到dispatch的订阅中,
     *
     *
     * view 对应的类名 当作 key
     * mRxBus 进行订阅,订阅事件当作 value
     * view 添加到rxStoreMap中之后,bus发送数据,在观察者中会回调rxViewDispatch的回调方法
     *
     * @param rxView
     * @param <T>
    </T> */
    fun <T : RxViewDispatch> subscribeRxView(rxView: T) {
        //获取传入的Object的名字
        val rxViewTag = rxView.javaClass.simpleName
        //获取Map中Object名字对应的value 监听者
        val disposable = mRxStoreMap[rxViewTag]
        //如果监听者空或者没订阅被监听者,生成一个新的监听者,并将他添加到 storeMap中
        if (disposable == null || disposable.isDisposed) {
            //获取rxBus实例,是一个Observable(被监听者)的子类对象
            //Subject=new SerializedSubject<>(PublishSubject.create())
            //会把在订阅(subscribe())发生的时间点之后来自原始Observable的数据发射给观察者
            mRxStoreMap.put(rxViewTag, mRxBus.get()
                    .onBackpressureBuffer()
                    .filter { o -> o is RxStoreChange }
                    .observeOn(AndroidSchedulers.mainThread())
                    //调用Activity,Fragment,View等所有实现了RxViewDispatch的类对象的onRxStoreChange方法
                    .subscribe { o -> rxView.onRxStoreChanged(o as RxStoreChange) })
        }
        subscribeRxError(rxView)
    }

    /**
     * 判断是否注册
     *
     * @param object
     * @param <T>
     * @return `true` object对应的Subscription不为空且已经注册, `false` otherwise
    </T> */
    fun <T : RxViewDispatch> isSubscribeRxView(`object`: T): Boolean {
        //获取传入的Object的名字
        val tag = `object`.javaClass.simpleName
        //获取Map中Object名字对应的value 监听者
        val disposable = mRxStoreMap[tag]
        return disposable != null && !disposable.isDisposed
    }

    /**
     * 解除rxStore的注册
     *
     * @param object
     * @param <T>
    </T> */
    fun <T : RxActionDispatch> unSubscribeRxStore(`object`: T) {
        val tag = `object`.javaClass.simpleName
        val disposable = mRxActionMap[tag]
        if (disposable != null && !disposable.isDisposed) {
            disposable.dispose()
            mRxActionMap.remove(tag)
        }
    }

    /**
     * 解除错误注册
     *
     * @param object
     * @param <T>
    </T> */
    fun <T : RxViewDispatch> unSubscribeRxError(`object`: T) {
        val tag = `object`.javaClass.simpleName + "_error"
        val disposable = mRxActionMap[tag]
        if (disposable != null && !disposable.isDisposed) {
            disposable.dispose()
            mRxActionMap.remove(tag)
        }
    }

    /**
     * 将view解除注册
     *
     * @param object
     * @param <T>
    </T> */
    fun <T : RxViewDispatch> unSubscribeRxView(`object`: T) {
        val tag = `object`.javaClass.simpleName
        val disposable = mRxStoreMap[tag]
        if (disposable != null && !disposable.isDisposed) {
            disposable.dispose()
            mRxStoreMap.remove(tag)
        }
        unSubscribeRxError(`object`)
    }

    /**
     * 解除所有的注册
     */
    @Synchronized
    fun unSubscribeAll() {
        for (disposable in mRxActionMap.values)
            disposable.dispose()
        for (disposable in mRxStoreMap.values)
            disposable.dispose()
        mRxActionMap.clear()
        mRxStoreMap.clear()
    }

    /**
     * 1:发送action变化,到所有订阅的store
     *
     * @param action
     */
    fun postRxAction(action: RxAction) {
        mRxBus.send(action)
    }

    /**
     * 2:发送store变化
     *
     * @param storeChange
     */
    fun postRxStoreChange(storeChange: RxStoreChange) {
        mRxBus.send(storeChange)
    }

    companion object {

        private var sInstance: Dispatcher? = null

        @Synchronized
        fun getInstance(rxBus: RxBus): Dispatcher {
            if (sInstance == null) sInstance = Dispatcher(rxBus)
            return sInstance
        }
    }
}
