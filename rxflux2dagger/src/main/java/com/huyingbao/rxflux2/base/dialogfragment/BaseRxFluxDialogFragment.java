package com.huyingbao.rxflux2.base.dialogfragment;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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
public abstract class BaseRxFluxDialogFragment extends BaseDialogFragment implements RxViewDispatch {
    @Inject
    protected RxFlux mRxFlux;

    @Override
    public void onStart() {
        super.onStart();
        // 注册rxstore
        List<RxStore> rxStoreList = getRxStoreListToRegister();
        if (rxStoreList != null)
            for (RxStore rxStore : rxStoreList)
                rxStore.register();
        // 注册view
        mRxFlux.getDispatcher().subscribeRxView(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        // 解除view注册
        mRxFlux.getDispatcher().unSubscribeRxView(this);
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
