package com.huyingbao.simple.main;

import android.os.Bundle;

import com.huyingbao.simple.R;
import com.huyingbao.rxflux2.base.fragment.BaseFragment;

public class MainFragment extends BaseFragment {

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        initActionBar();
    }
}
