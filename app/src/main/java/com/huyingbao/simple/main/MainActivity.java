package com.huyingbao.simple.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.huyingbao.rxflux2.action.RxAction;
import com.huyingbao.rxflux2.base.activity.BaseRxFluxToolbarActivity;
import com.huyingbao.rxflux2.constant.Actions;
import com.huyingbao.rxflux2.constant.ActionsKeys;
import com.huyingbao.rxflux2.store.RxStore;
import com.huyingbao.rxflux2.store.RxStoreChange;
import com.huyingbao.simple.R;
import com.huyingbao.simple.main.store.MainStore;

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
        toMain();
    }

    @Override
    public void onRxStoreChanged(@NonNull RxStoreChange rxStoreChange) {
        RxAction rxAction = rxStoreChange.getRxAction();
        switch (rxAction.getType()) {
            case Actions.TO_GIT_REPO_LIST:
                toProductList();
                break;
            case Actions.TO_GIT_USER:
                toShop(rxAction.get(ActionsKeys.USER_ID));
                break;
        }
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
     * 到主页面
     */
    private void toMain() {
        getFragmentTransaction(R.id.fl_content)
                .add(R.id.fl_content, MainFragment.newInstance())
                .commit();
    }

    /**
     * 到列表页面
     */
    private void toProductList() {
        getFragmentTransaction(R.id.fl_content)
                .add(R.id.fl_content, ProductListFragment.newInstance())
                .commit();
    }

    /**
     * 到店铺信息页面
     *
     * @param userId
     */
    private void toShop(int userId) {
        getFragmentTransaction(R.id.fl_content)
                .add(R.id.fl_content, ShopFragment.newInstance(userId))
                .commit();
    }
}
