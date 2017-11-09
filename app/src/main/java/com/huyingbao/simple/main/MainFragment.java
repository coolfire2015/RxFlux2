package com.huyingbao.simple.main;

import android.os.Bundle;

import com.huyingbao.rxflux2.base.fragment.BaseFragment;
import com.huyingbao.rxflux2.constant.Actions;
import com.huyingbao.simple.R;

import butterknife.OnClick;

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

    @OnClick(R.id.btn_main_to_list)
    public void toGitRepoList() {
        mActionCreator.postLocalAction(Actions.TO_GIT_REPO_LIST);
    }
}
