package com.hardsoftstudio.rxflux.util;

import android.support.v4.util.ArrayMap;
import android.support.v4.util.Pair;

import com.hardsoftstudio.rxflux.action.RxAction;

import rx.Subscription;

/**
 * Utility class to handle subscriptions
 * 订阅管理类
 */
public final class SubscriptionManager {

    private static SubscriptionManager instance;
    /**
     * 管理订阅的arraymap
     */
    private ArrayMap<String, Pair<Integer, Subscription>> mMap;

    private SubscriptionManager() {
        mMap = new ArrayMap<>();
    }

    public static synchronized SubscriptionManager getInstance() {
        if (instance == null) instance = new SubscriptionManager();
        return instance;
    }

    /**
     * Given an action and a subscription, add the new subscription and unsubscribe if there
     * was an existing one.
     * 添加一个action和subscription,如果已经有了一个对应action 的订阅,则取消订阅
     */
    public void add(RxAction action, Subscription subscription) {
        Pair<Integer, Subscription> old = mMap.put(action.getType(), getPair(action, subscription));
        if (old != null && !old.second.isUnsubscribed()) old.second.unsubscribe();
    }

    /**
     * Remove an rxAction and unsubscribe from it
     * 从管理器中取消订阅
     */
    public void remove(RxAction action) {
        Pair<Integer, Subscription> old = mMap.remove(action.getType());
        if (old != null && !old.second.isUnsubscribed()) old.second.unsubscribe();
    }

    /**
     * Checks if the action (with the same params) is already running a subscription
     * 检查action是否已经运行一个subscription
     *
     * @return true if the exact action is inside the map and running
     */
    public boolean contains(RxAction action) {
        Pair<Integer, Subscription> old = mMap.get(action.getType());
        return old != null && old.first == action.hashCode() && !old.second.isUnsubscribed();
    }

    /**
     * Clear all the subscriptions
     * 清除所有的subscriptions
     */
    public synchronized void clear() {
        if (mMap.isEmpty()) return;

        for (Pair<Integer, Subscription> pair : mMap.values())
            if (!pair.second.isUnsubscribed())
                pair.second.unsubscribe();
    }

    /**
     * 创建一个新的pair
     *
     * @param action       转变成hashcode
     * @param subscription
     * @return
     */
    private Pair<Integer, Subscription> getPair(RxAction action, Subscription subscription) {
        return new Pair<>(action.hashCode(), subscription);
    }
}
