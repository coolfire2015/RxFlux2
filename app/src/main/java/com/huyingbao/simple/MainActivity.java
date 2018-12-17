package com.huyingbao.simple;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.huyingbao.rxflux2.base.activity.BaseRxFluxToolbarActivity;
import com.huyingbao.rxflux2.store.RxStore;
import com.huyingbao.rxflux2.store.RxStoreChange;
import com.huyingbao.simple.store.MainStore;

import org.greenrobot.eventbus.Subscribe;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class MainActivity extends BaseRxFluxToolbarActivity {
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
    public void afterCreate(Bundle savedInstanceState) {
        toProductList();
    }

    @Override
    @Subscribe
    public void onRxStoreChanged(@NonNull RxStoreChange rxStoreChange) {
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
        getFragmentTransaction(R.id.fl_content)
                .add(R.id.fl_content, ProductListFragment.newInstance())
                .commit();
    }
}
