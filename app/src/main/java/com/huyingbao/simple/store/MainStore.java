package com.huyingbao.simple.store;

import com.huyingbao.rxflux2.action.RxAction;
import com.huyingbao.simple.action.Actions;
import com.huyingbao.rxflux2.constant.ActionsKeys;
import com.huyingbao.rxflux2.dispatcher.Dispatcher;
import com.huyingbao.rxflux2.store.RxStore;
import com.huyingbao.rxflux2.store.RxStoreChange;
import com.huyingbao.simple.model.GanResponse;
import com.huyingbao.simple.model.Product;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

public class MainStore extends RxStore {
    private GanResponse<Product> mProductList;

    public MainStore(Dispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    @Subscribe
    public void onRxAction(RxAction rxAction) {
        switch (rxAction.getType()) {
            case Actions.GET_GIT_REPO_LIST:
                mProductList = rxAction.get(ActionsKeys.RESPONSE);
                break;
            default://此处不能省略，不是本模块的逻辑，直接返回，不发送RxStoreChange
                return;
        }
        postChange(new RxStoreChange(getClass().getSimpleName(), rxAction));
    }

    public List<Product> getProductList() {
        return mProductList.getResults();
    }
}
