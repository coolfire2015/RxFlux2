package com.huyingbao.rxflux2.store;

import com.huyingbao.rxflux2.action.RxAction;

/**
 * Pos a new event to notify that the store has changed
 */
public class RxStoreChange {

    private String mStoreId;
    private RxAction mRxAction;

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
