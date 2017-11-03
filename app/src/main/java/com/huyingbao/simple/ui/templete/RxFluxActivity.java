package com.huyingbao.simple.ui.templete;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.huyingbao.simple.R;
import com.huyingbao.simple.ui.templete.store.RxFluxStore;
import com.huyingbao.rxflux2.base.activity.BaseRxFluxToolbarActivity;
import com.huyingbao.rxflux2.store.RxStore;
import com.huyingbao.rxflux2.store.RxStoreChange;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class RxFluxActivity extends BaseRxFluxToolbarActivity {
    @Inject
    RxFluxStore mStore;

    public static Intent newIntent(Context content) {
        Intent intent = new Intent(content, RxFluxActivity.class);
        return intent;
    }

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        getFragmentTransaction(R.id.fl_content)
                .add(R.id.fl_content, RxFluxFragment.newInstance())
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
