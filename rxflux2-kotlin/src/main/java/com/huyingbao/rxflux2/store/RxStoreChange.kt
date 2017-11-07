package com.huyingbao.rxflux2.store

import com.huyingbao.rxflux2.action.RxAction

/**
 * Pos a new event to notify that the store has changed
 */
class RxStoreChange(val storeId: String, val rxAction: RxAction)
