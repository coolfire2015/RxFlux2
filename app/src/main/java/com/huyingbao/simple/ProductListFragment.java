package com.huyingbao.simple;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.huyingbao.rxflux2.base.fragment.BaseRxFluxListFragment;
import com.huyingbao.rxflux2.store.RxStore;
import com.huyingbao.rxflux2.store.RxStoreChange;
import com.huyingbao.simple.action.MainAction;
import com.huyingbao.simple.action.MainActionCreator;
import com.huyingbao.simple.adapter.ProductAdapter;
import com.huyingbao.simple.model.Product;
import com.huyingbao.simple.action.MainStore;

import org.greenrobot.eventbus.Subscribe;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * 列表展示
 * Created by liujunfeng on 2017/11/9.
 */
public class ProductListFragment extends BaseRxFluxListFragment<Product> {
    @Inject
    MainStore mStore;
    @Inject
    MainActionCreator mMainActionCreator;

    public static ProductListFragment newInstance() {
        ProductListFragment fragment = new ProductListFragment();
        return fragment;
    }

    @Override
    public void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        super.afterCreate(savedInstanceState);
    }

    @Override
    @Subscribe(sticky = true)
    public void onRxStoreChanged(@NonNull RxStoreChange change) {
        switch (change.getRxAction().getType()) {
            case MainAction.GET_GIT_REPO_LIST:
                showDataList(mStore.getProductList());
                break;
        }
    }

    @Nullable
    @Override
    public List<RxStore> getRxStoreListToRegister() {
        return Collections.singletonList(mStore);
    }

    @Override
    protected void initAdapter() {
        mAdapter = new ProductAdapter(mDataList);
    }

    @Override
    protected void initRecyclerView() {
        super.initRecyclerView();
    }

    @Override
    protected void getDataList(int index) {
        mMainActionCreator.getProductList();
    }
}
