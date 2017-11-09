package com.huyingbao.simple.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.huyingbao.rxflux2.base.fragment.BaseRxFluxListFragment;
import com.huyingbao.rxflux2.constant.Actions;
import com.huyingbao.rxflux2.constant.ActionsKeys;
import com.huyingbao.rxflux2.store.RxStore;
import com.huyingbao.rxflux2.store.RxStoreChange;
import com.huyingbao.simple.main.adapter.GitRepoAdapter;
import com.huyingbao.simple.main.model.GitRepo;
import com.huyingbao.simple.main.store.MainStore;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by liujunfeng on 2017/11/9.
 */
public class GitRepoListFragment extends BaseRxFluxListFragment<GitRepo> {
    @Inject
    MainStore mStore;

    public static GitRepoListFragment newInstance() {
        GitRepoListFragment fragment = new GitRepoListFragment();
        return fragment;
    }

    @Override
    public void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        super.afterCreate(savedInstanceState);
        initActionBar("列表展示");
    }

    @Override
    public void onRxStoreChanged(@NonNull RxStoreChange change) {
        switch (change.getRxAction().getType()) {
            case Actions.GET_GIT_REPO_LIST:
                showDataList(mStore.getGitRepoList());
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
        mAdapter = new GitRepoAdapter(mDataList);
    }

    @Override
    protected void initRecyclerView() {
        super.initRecyclerView();
        mRvContent.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                mActionCreator.postLocalAction(Actions.TO_GIT_USER, ActionsKeys.USER_ID,
                        mDataList.get(position).getOwner().getId());
            }
        });
    }

    @Override
    protected void getDataList(int index) {
        mActionCreator.getGitRepoList();
    }
}
