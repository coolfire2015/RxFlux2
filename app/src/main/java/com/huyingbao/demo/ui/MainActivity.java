package com.huyingbao.demo.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.hardsoftstudio.rxflux.store.RxStore;
import com.hardsoftstudio.rxflux.store.RxStoreChange;
import com.huyingbao.demo.R;
import com.huyingbao.demo.base.activity.BaseRxFluxActivity;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by liujunfeng on 2017/8/8.
 */

public class MainActivity extends BaseRxFluxActivity {
    @Inject
    MainStore mMainStore;

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
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_content, MainFragment.newInstance())
                .commit();
    }

    @Override
    public void onRxStoreChanged(@NonNull RxStoreChange change) {

    }

    @Nullable
    @Override
    public List<RxStore> getRxStoreListToRegister() {
        return Arrays.asList(mMainStore);
    }

    @Nullable
    @Override
    public List<RxStore> getRxStoreListToUnRegister() {
        return Arrays.asList(mMainStore);
    }


}
