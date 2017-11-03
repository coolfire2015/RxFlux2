package com.huyingbao.simple.ui.main;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.huyingbao.rxflux2.base.fragment.BaseRxFluxListFragment;
import com.huyingbao.rxflux2.constant.Actions;
import com.huyingbao.rxflux2.store.RxStore;
import com.huyingbao.rxflux2.store.RxStoreChange;
import com.huyingbao.simple.ui.main.adapter.GithubAdapter;
import com.huyingbao.simple.ui.main.model.GitHubRepo;
import com.huyingbao.simple.ui.main.store.MainStore;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by liujunfeng on 2017/8/15.
 */

public class MainFragment extends BaseRxFluxListFragment<GitHubRepo> {
    @Inject
    MainStore mMainStore;

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    public void onRxStoreChanged(@NonNull RxStoreChange change) {
        switch (change.getRxAction().getType()) {
            case Actions.GET_PUBLIC_REPOS:
                showDataList(mMainStore.getGitHubRepoList());
                break;
        }
    }

    @Nullable
    @Override
    public List<RxStore> getRxStoreListToRegister() {
        return Collections.singletonList(mMainStore);
    }

    @Override
    protected void initAdapter() {
        mAdapter = new GithubAdapter(mDataList);
    }

    @Override
    protected void getDataList(int index) {
        mActionCreator.getPublicRepositories();
    }
}
