package com.huyingbao.demo.base.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.hardsoftstudio.rxflux.RxFlux;
import com.hardsoftstudio.rxflux.action.RxError;
import com.hardsoftstudio.rxflux.dispatcher.RxViewDispatch;
import com.hardsoftstudio.rxflux.store.RxStore;

import java.util.List;

import javax.inject.Inject;

/**
 * 所有需要接收store的Fragment需要继承该类,实现RxFlux接口
 * Created by liujunfeng on 2017/1/1.
 */
public abstract class BaseRxFluxFragment extends BaseFragment implements RxViewDispatch {
    @Inject
    RxFlux mRxFlux;

    /**
     * 注册RxStore
     * 因为fragment不能像activity通过RxFlux根据生命周期在启动的时候,
     * 调用getRxStoreListToRegister,注册RxStore,只能手动注册
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //注册rxstore
        List<RxStore> rxStoreList = getRxStoreListToRegister();
        if (rxStoreList != null) for (RxStore rxStore : rxStoreList) rxStore.register();
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        //注册view
        mRxFlux.getDispatcher().subscribeRxView(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        //解除view注册
        mRxFlux.getDispatcher().unsubscribeRxView(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //解除RxStore注册
        List<RxStore> rxStoreList = getRxStoreListToUnRegister();
        if (rxStoreList != null) for (RxStore rxStore : rxStoreList) rxStore.unregister();
    }

    @Override
    public void onRxError(@NonNull RxError error) {
    }

    /**
     * 需要解除注册rxstore list 在activity创建的时候调用该方法,
     * 从 dispatcher 解除注册rxstore list
     *
     * @return
     */
    @Nullable
    @Override
    public List<RxStore> getRxStoreListToUnRegister() {
        return null;
    }

    /**
     * 注入Injector
     */
    public abstract void initInjector();

    /**
     * 设置view
     *
     * @return
     */
    public abstract int getLayoutId();

    /**
     * 创建之后的操作
     *
     * @param savedInstanceState
     */
    public abstract void afterCreate(Bundle savedInstanceState);
}
