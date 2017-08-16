package com.huyingbao.rxflux2.dispatcher;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.huyingbao.rxflux2.action.RxError;
import com.huyingbao.rxflux2.store.RxStore;
import com.huyingbao.rxflux2.store.RxStoreChange;

import java.util.List;

/**
 * Created by marcel on 10/09/15.
 * <p>
 * Activities implementing this interface will be part of the RxFlux flow. Implement the methods in
 * order to get the proper callbacks and un/register stores accordingly to Flux flow.
 */
public interface RxViewDispatch {

    /**
     * All the stores will call this event after they process an action and the store change it.
     * The view can react and request the needed data
     */
    void onRxStoreChanged(@NonNull RxStoreChange change);

    /**
     * Called when an error occur in some point of the flux flow.
     *
     * @param error {@link RxError} containing the information for that specific error
     */
    void onRxError(@NonNull RxError error);

    /**
     * The stores needs to be register in order to get Actions, this method will be called once the activity
     * get create. Return the store list with the RxStores that need to be registered for this specific view.
     * No need to check if one or many RxStore on the list were registered. RxFlux will ignore those.
     * <p>
     * For example, a base method could create a list with the common stores and each activity will add stores
     * in case they are needed.
     * activity onCreate()的时候调用该方法,
     *
     * @return list of {@link RxStore} to be registered, can be null.
     */
    @Nullable
    List<RxStore> getRxStoreListToRegister();

    /**
     * Return a list of RxStore that contains the RxStore you want to specifically unregister when the
     * activity implementing this interface gets destroyed.
     * Notice that if the Application is destroyed RxFlux will automatically unregister any store to avoid leaks.
     *
     * @return list of {@link RxStore} to be unregister, can be null
     * activity onDistroy()
     */
    @Nullable
    List<RxStore> getRxStoreListToUnRegister();
}
