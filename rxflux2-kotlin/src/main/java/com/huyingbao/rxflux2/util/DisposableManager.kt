package com.huyingbao.rxflux2.util

import android.support.v4.util.ArrayMap
import android.support.v4.util.Pair

import com.huyingbao.rxflux2.action.RxAction

import io.reactivex.disposables.Disposable


/**
 * Utility class to handle disposables
 * 订阅管理类
 */
class DisposableManager private constructor() {
    /**
     * 管理订阅的ArrayMap
     */
    private val mMap: ArrayMap<String, Pair<Int, Disposable>>

    init {
        mMap = ArrayMap()
    }

    /**
     * Given an action and a disposable, add the new disposable and unSubscribe if there
     * was an existing one.
     * 添加一个action和disposable,如果已经有了一个对应action 的订阅,则取消订阅
     */
    fun add(action: RxAction, disposable: Disposable) {
        val old = mMap.put(action.type, getPair(action, disposable))
        if (old != null && !old.second!!.isDisposed) old.second!!.dispose()
    }

    /**
     * Remove an rxAction and unSubscribe from it
     * 从管理器中取消订阅
     */
    fun remove(action: RxAction) {
        val old = mMap.remove(action.type)
        if (old != null && !old.second!!.isDisposed) old.second!!.dispose()
    }

    /**
     * Checks if the action (with the same params) is already running a disposable
     * 检查action是否已经运行一个disposable
     *
     * @return true if the exact action is inside the map and running
     */
    operator fun contains(action: RxAction): Boolean {
        val old = mMap[action.type]
        return old != null && old.first == action.hashCode() && !old.second!!.isDisposed
    }

    /**
     * Clear all the disposables
     * 清除所有的disposables
     */
    @Synchronized
    fun clear() {
        if (mMap.isEmpty) return
        for (pair in mMap.values)
            if (!pair.second!!.isDisposed)
                pair.second!!.dispose()
    }

    /**
     * 创建一个新的pair
     *
     * @param action     转变成hashcode
     * @param disposable
     * @return
     */
    private fun getPair(action: RxAction, disposable: Disposable): Pair<Int, Disposable> {
        return Pair(action.hashCode(), disposable)
    }

    companion object {

        private var sInstance: DisposableManager? = null

        val instance: DisposableManager
            @Synchronized get() {
                if (sInstance == null) sInstance = DisposableManager()
                return sInstance
            }
    }
}
