package com.huyingbao.simple.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.huyingbao.simple.R;
import com.huyingbao.simple.main.store.MainStore;
import com.huyingbao.rxflux2.base.activity.BaseRxFluxToolbarActivity;
import com.huyingbao.rxflux2.store.RxStore;
import com.huyingbao.rxflux2.store.RxStoreChange;

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
        getFragmentTransaction(R.id.fl_content)
                .add(R.id.fl_content, MainFragment.newInstance())
                .commit();
    }

    @Override
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
}
