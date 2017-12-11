package com.huyingbao.simple;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.huyingbao.rxflux2.base.fragment.BaseRxFluxFragment;
import com.huyingbao.rxflux2.constant.Actions;
import com.huyingbao.rxflux2.constant.ActionsKeys;
import com.huyingbao.rxflux2.store.RxStore;
import com.huyingbao.rxflux2.store.RxStoreChange;
import com.huyingbao.simple.model.Shop;
import com.huyingbao.simple.store.MainStore;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * 店铺信息
 * Created by liujunfeng on 2017/11/9.
 */
public class ShopFragment extends BaseRxFluxFragment {
    @Inject
    MainStore mStore;

    @BindView(R.id.tv_shop_name)
    TextView mTvShopName;
    @BindView(R.id.tv_shop_login)
    TextView mTvShopLogin;
    @BindView(R.id.tv_shop_statistics)
    TextView mTvShopStatistics;

    public static ShopFragment newInstance(int userId) {
        Bundle bundle = new Bundle();
        bundle.putInt(ActionsKeys.USER_ID, userId);
        ShopFragment fragment = new ShopFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_shop;
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        initActionBar("店铺信息");
        mActionCreator.getShop(mContext, getArguments().getInt(ActionsKeys.USER_ID));
    }

    @Override
    public void onRxStoreChanged(@NonNull RxStoreChange rxStoreChange) {
        switch (rxStoreChange.getRxAction().getType()) {
            case Actions.GET_GIT_USER:
                showShopInfo(mStore.getShop());
                break;
        }
    }

    @Nullable
    @Override
    public List<RxStore> getRxStoreListToRegister() {
        return Collections.singletonList(mStore);
    }

    /**
     * 显示店铺信息
     *
     * @param gitUser
     */
    private void showShopInfo(Shop gitUser) {
        mTvShopLogin.setText(gitUser.getCode() + "");
        mTvShopName.setText(gitUser.getShopName());
        mTvShopStatistics.setText(gitUser.getShopType() + "");
    }
}
