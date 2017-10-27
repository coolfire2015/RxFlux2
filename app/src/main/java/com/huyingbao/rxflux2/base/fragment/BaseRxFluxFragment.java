package com.huyingbao.rxflux2.base.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.huyingbao.rxflux2.RxFlux;
import com.huyingbao.rxflux2.action.RxError;
import com.huyingbao.rxflux2.dispatcher.RxViewDispatch;
import com.huyingbao.rxflux2.store.RxStore;

import java.util.List;

import javax.inject.Inject;

/**
 * 所有需要接收store的Fragment需要继承该类,实现RxFlux接口
 * Created by liujunfeng on 2017/1/1.
 */
public abstract class BaseRxFluxFragment extends BaseFragment implements RxViewDispatch {
    @Inject
    protected RxFlux mRxFlux;

    /**
     * 注册RxStore
     * 因为fragment不能像activity通过RxFlux根据生命周期在启动的时候,
     * 调用getRxStoreListToRegister,注册RxStore,只能手动注册
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // 注册RxStore
        List<RxStore> rxStoreList = getRxStoreListToRegister();
        if (rxStoreList != null)
            for (RxStore rxStore : rxStoreList)
                rxStore.register();
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        // 注册view
        mRxFlux.getDispatcher().subscribeRxView(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        // 解除view注册
        mRxFlux.getDispatcher().unSubscribeRxView(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 解除RxStore注册
        List<RxStore> rxStoreList = getRxStoreListToUnRegister();
        if (rxStoreList != null)
            for (RxStore rxStore : rxStoreList)
                rxStore.unregister();
    }

    @Override
    public void onRxError(@NonNull RxError error) {
    }

    @Nullable
    @Override
    public List<RxStore> getRxStoreListToUnRegister() {
        return null;
    }
}
