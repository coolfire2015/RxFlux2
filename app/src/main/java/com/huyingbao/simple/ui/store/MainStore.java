package com.huyingbao.simple.ui.store;

import com.huyingbao.rxflux2.constant.Actions;
import com.huyingbao.rxflux2.constant.ActionsKeys;
import com.huyingbao.simple.ui.model.GitHubRepo;
import com.huyingbao.rxflux2.action.RxAction;
import com.huyingbao.rxflux2.dispatcher.Dispatcher;
import com.huyingbao.rxflux2.store.RxStore;
import com.huyingbao.rxflux2.store.RxStoreChange;

import java.util.List;

/**
 * Created by liujunfeng on 2017/8/15.
 */

public class MainStore extends RxStore {
    List<GitHubRepo> mGitHubRepoList;

    public MainStore(Dispatcher dispatcher) {
        super(dispatcher);
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
