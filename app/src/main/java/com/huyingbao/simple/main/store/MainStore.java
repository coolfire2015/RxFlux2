package com.huyingbao.simple.main.store;

import com.huyingbao.rxflux2.action.RxAction;
import com.huyingbao.rxflux2.constant.Actions;
import com.huyingbao.rxflux2.constant.ActionsKeys;
import com.huyingbao.rxflux2.dispatcher.Dispatcher;
import com.huyingbao.rxflux2.store.RxStore;
import com.huyingbao.rxflux2.store.RxStoreChange;
import com.huyingbao.simple.main.model.GitRepo;
import com.huyingbao.simple.main.model.GitUser;

import java.util.List;

public class MainStore extends RxStore {
    private List<GitRepo> mGitRepoList;
    private GitUser mGitUser;

    public MainStore(Dispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void onRxAction(RxAction rxAction) {
        switch (rxAction.getType()) {
            case Actions.GET_GIT_REPO_LIST:
                mGitRepoList = rxAction.get(ActionsKeys.RESPONSE);
                break;
            case Actions.GET_GIT_USER:
                mGitUser = rxAction.get(ActionsKeys.RESPONSE);
                break;
            case Actions.TO_GIT_REPO_LIST:
            case Actions.TO_GIT_USER:
                break;
            default://此处不能省略，不是本模块的逻辑，直接返回，不发送RxStoreChange
                return;
        }
        postChange(new RxStoreChange(getClass().getSimpleName(), rxAction));
    }

    public List<GitRepo> getGitRepoList() {
        return mGitRepoList;
    }

    public GitUser getGitUser() {
        return mGitUser;
    }
}
