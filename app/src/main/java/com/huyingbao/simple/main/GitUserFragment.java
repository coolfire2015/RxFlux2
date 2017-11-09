package com.huyingbao.simple.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.huyingbao.rxflux2.base.fragment.BaseRxFluxFragment;
import com.huyingbao.rxflux2.constant.Actions;
import com.huyingbao.rxflux2.constant.ActionsKeys;
import com.huyingbao.rxflux2.store.RxStore;
import com.huyingbao.rxflux2.store.RxStoreChange;
import com.huyingbao.simple.R;
import com.huyingbao.simple.main.model.GitUser;
import com.huyingbao.simple.main.store.MainStore;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class GitUserFragment extends BaseRxFluxFragment {
    @Inject
    MainStore mStore;

    @BindView(R.id.tv_git_user_name)
    TextView mTvGitUserName;
    @BindView(R.id.tv_git_user_login)
    TextView mTvGitUserLogin;
    @BindView(R.id.tv_git_user_statistics)
    TextView mTvGitUserStatistics;

    public static GitUserFragment newInstance(int userId) {
        Bundle bundle = new Bundle();
        bundle.putInt(ActionsKeys.USER_ID, userId);
        GitUserFragment fragment = new GitUserFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_git_user;
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        initActionBar("用户信息");
        mActionCreator.getGitUser(mContext, getArguments().getInt(ActionsKeys.USER_ID));
    }

    @Override
    public void onRxStoreChanged(@NonNull RxStoreChange rxStoreChange) {
        switch (rxStoreChange.getRxAction().getType()) {
            case Actions.GET_GIT_USER:
                showGitUserInfo(mStore.getGitUser());
                break;
        }
    }

    @Nullable
    @Override
    public List<RxStore> getRxStoreListToRegister() {
        return Collections.singletonList(mStore);
    }

    private void showGitUserInfo(GitUser gitUser) {
        mTvGitUserLogin.setText(gitUser.getLogin());
        mTvGitUserName.setText(gitUser.getName());
        mTvGitUserStatistics.setText(gitUser.getName());
    }
}
