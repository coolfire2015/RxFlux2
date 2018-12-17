package com.huyingbao.simple;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.huyingbao.rxflux2.action.RxError;
import com.huyingbao.rxflux2.base.activity.BaseRxFluxActivity;
import com.huyingbao.rxflux2.store.RxStore;
import com.huyingbao.rxflux2.store.RxStoreChange;
import com.huyingbao.simple.action.MainStore;

import org.greenrobot.eventbus.Subscribe;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class MainActivity extends BaseRxFluxActivity {
    @Inject
    MainStore mStore;

    public static Intent newIntent(Context content) {
        Intent intent = new Intent(content, MainActivity.class);
        return intent;
    }

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_fragment_base;
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        toProductList();
    }

    @Override
    @Subscribe
    public void onRxStoreChanged(@NonNull RxStoreChange rxStoreChange) {
    }

    @Override
    public void onRxError(@NonNull RxError error) {

    }

    @Nullable
    @Override
    public List<RxStore> getRxStoreListToRegister() {
        return Collections.singletonList(mStore);
    }

    @Nullable
    @Override
    public List<RxStore> getRxStoreListToUnRegister() {
        return Collections.singletonList(mStore);
    }

    /**
     * 到列表页面
     */
    private void toProductList() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_content, ProductListFragment.newInstance())
                .commit();
    }
}
