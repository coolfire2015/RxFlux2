package com.huyingbao.demo.stores;

import com.hardsoftstudio.rxflux.action.RxAction;
import com.hardsoftstudio.rxflux.dispatcher.Dispatcher;
import com.hardsoftstudio.rxflux.store.RxStore;
import com.hardsoftstudio.rxflux.store.RxStoreChange;
import com.huyingbao.demo.actions.Actions;
import com.huyingbao.demo.actions.ActionsKeys;
import com.huyingbao.demo.model.GitHubRepo;

import java.util.ArrayList;

/**
 * Created by marcel on 10/09/15.
 */
public class RepositoriesStore extends RxStore {

    public static final String ID = "RepositoriesStore";
    private static RepositoriesStore instance;
    private ArrayList<GitHubRepo> gitHubRepos;

    private RepositoriesStore(Dispatcher dispatcher) {
        super(dispatcher);
    }

    public static synchronized RepositoriesStore get(Dispatcher dispatcher) {
        if (instance == null) instance = new RepositoriesStore(dispatcher);
        return instance;
    }

    @Override
    public void onRxAction(RxAction action) {
        switch (action.getType()) {
            case Actions.GET_PUBLIC_REPOS:
                this.gitHubRepos = action.get(ActionsKeys.PUBLIC_REPOS);
                break;
            default: // IMPORTANT if we don't modify the store just ignore
                return;
        }
        postChange(new RxStoreChange(ID, action));
    }

    public ArrayList<GitHubRepo> getRepositories() {
        return gitHubRepos == null ? new ArrayList<GitHubRepo>() : gitHubRepos;
    }
}
