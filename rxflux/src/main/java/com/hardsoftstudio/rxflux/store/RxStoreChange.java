package com.hardsoftstudio.rxflux.store;

import com.hardsoftstudio.rxflux.action.RxAction;

/**
 * Pos a new event to notify that the store has changed
 */
public class RxStoreChange {

    String mStoreId;
    RxAction mRxAction;

    public RxStoreChange(String storeId, RxAction rxAction) {
        this.mStoreId = storeId;
        this.mRxAction = rxAction;
    }

    public RxAction getRxAction() {
        return mRxAction;
    }

    public String getStoreId() {
        return mStoreId;
    }
}
