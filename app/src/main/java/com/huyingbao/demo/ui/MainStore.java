package com.huyingbao.demo.ui;

import com.hardsoftstudio.rxflux.action.RxAction;
import com.hardsoftstudio.rxflux.dispatcher.Dispatcher;
import com.hardsoftstudio.rxflux.store.RxStoreChange;
import com.huyingbao.demo.action.Actions;
import com.huyingbao.demo.action.ActionsKeys;
import com.huyingbao.demo.model.GitHubRepo;
import com.huyingbao.demo.store.BaseRxStore;

import java.util.List;

/**
 * Created by liujunfeng on 2017/8/15.
 */

public class MainStore extends BaseRxStore {
    List<GitHubRepo> mGitHubRepoList;

    public MainStore(Dispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void initInjector() {
        mApplicationComponent.inject(this);
    }

    @Override
    public void onRxAction(RxAction action) {
        switch (action.getType()) {
            case Actions.GET_PUBLIC_REPOS:
                mGitHubRepoList = action.get(ActionsKeys.RESPONSE);
                break;
            default://必须有,接收到非自己处理的action返回
                return;
        }
        postChange(new RxStoreChange(getClass().getSimpleName(), action));//只发送自己处理的action
    }

    public List<GitHubRepo> getGitHubRepoList() {
        return mGitHubRepoList;
    }
}
