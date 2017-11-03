package com.huyingbao.simple.ui.templete;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.huyingbao.simple.R;
import com.huyingbao.simple.ui.templete.store.RxFluxStore;
import com.huyingbao.rxflux2.base.fragment.BaseRxFluxFragment;
import com.huyingbao.rxflux2.store.RxStore;
import com.huyingbao.rxflux2.store.RxStoreChange;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class RxFluxFragment extends BaseRxFluxFragment {
    @Inject
    RxFluxStore mStore;

    public static RxFluxFragment newInstance() {
        RxFluxFragment fragment = new RxFluxFragment();
        return fragment;
    }

    @Override
    public void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_rx_flux;
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        initActionBar();
    }

    @Override
    public void onRxStoreChanged(@NonNull RxStoreChange rxStoreChange) {

    }

    @Nullable
    @Override
    public List<RxStore> getRxStoreListToRegister() {
        return Collections.singletonList(mStore);
    }
}
