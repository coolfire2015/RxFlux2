package com.huyingbao.rxflux2.action

import com.huyingbao.rxflux2.dispatcher.Dispatcher
import com.huyingbao.rxflux2.util.DisposableManager

import io.reactivex.disposables.Disposable


/**
 * This class must be extended in order to give useful functionality to create RxAction.
 * 这个类必须被继承,提供一个可以创建RxAction的方法.
 * 按钮被点击触发回调方法，在回调方法中调用ActionCreator提供的有语义的的方法，ActionCreator会根据传入参数创建Action并通过Dispatcher发送给Store，
 * 所有订阅了这个Action的Store会接收到订阅的Action并消化Action，
 * 然后Store会发送UI状态改变的事件给相关的Activity（或Fragment)，
 * Activity在收到状态发生改变的事件之后，开始更新UI（更新UI的过程中会从Store获取所有需要的数据）。
 */
abstract class RxActionCreator
/**
 * 构造方法,传入dispatcher和订阅管理器
 *
 * @param dispatcher
 * @param disposableManager
 */
(private val mDispatcher: Dispatcher,
 /**
  * RxJava 观察者的管理者
  */
 private val mDisposableManager: DisposableManager) {

    /**
     * 主要是为了和RxJava整合,用在调用网络接口api获取数据之后,被观察者得到数据,发生订阅关系,将返回的数据
     * 或者error封装成action,postAction或者postError出去
     * 订阅管理,将RxAction和Disposable添加到DisposableManager
     *
     * @param rxAction
     * @param disposable
     */
    protected fun addRxAction(rxAction: RxAction, disposable: Disposable) {
        mDisposableManager.add(rxAction, disposable)
    }

    /**
     * 订阅管理器是否已经有了该action
     *
     * @param rxAction
     * @return
     */
    protected fun hasRxAction(rxAction: RxAction): Boolean {
        return mDisposableManager.contains(rxAction)
    }

    /**
     * 订阅管理器,移除该action
     *
     * @param rxAction
     */
    private fun removeRxAction(rxAction: RxAction) {
        mDisposableManager.remove(rxAction)
    }

    /**
     * 创建新的RxAction
     *
     * @param actionId action type对应具体是什么样的方法
     * @param data     键值对型的参数pair-value parameters(Key - Object))
     * @return
     */
    protected fun newRxAction(actionId: String, vararg data: Any): RxAction {
        if (data != null) {
            if (data.size % 2 != 0)
                throw IllegalArgumentException("Data must be a valid list of key,value pairs")
        }
        val actionBuilder = RxAction.Builder(actionId)
        var i = 0
        while (i < data.size) {
            val key = data[i++] as String
            val value = data[i++]
            actionBuilder.put(key, value)
        }
        return actionBuilder.build()
    }

    /**
     * 通过调度器dispatcher将action推出去
     *
     * @param action
     */
    protected fun postRxAction(action: RxAction) {
        mDispatcher.postRxAction(action)
        removeRxAction(action)
    }

    /**
     * 通过调度器dispatcher将error action推出去
     *
     * @param action
     * @param throwable
     */
    protected fun postError(action: RxAction, throwable: Throwable) {
        mDispatcher.postRxAction(RxError.newRxError(action, throwable))
        removeRxAction(action)
    }
}
