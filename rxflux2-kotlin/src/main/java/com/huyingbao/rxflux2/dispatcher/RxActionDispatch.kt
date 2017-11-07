package com.huyingbao.rxflux2.dispatcher

import com.huyingbao.rxflux2.action.RxAction

/**
 * This interface must be implemented by the store
 * 所有的store必须实现该接口
 */
interface RxActionDispatch {

    /**
     * store在接收到RxAction时,调用该方法
     *
     * @param action
     */
    fun onRxAction(action: RxAction)
}
