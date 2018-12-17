package com.huyingbao.simple.action;

import com.huyingbao.rxflux2.RxFlux;
import com.huyingbao.rxflux2.action.RxAction;
import com.huyingbao.rxflux2.constant.ActionsKeys;
import com.huyingbao.rxflux2.inject.scope.PerActivity;
import com.huyingbao.rxflux2.store.RxStore;
import com.huyingbao.rxflux2.store.RxStoreChange;
import com.huyingbao.simple.model.GanResponse;
import com.huyingbao.simple.model.Product;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import javax.inject.Inject;

@PerActivity
public class MainStore extends RxStore {
    private GanResponse<Product> mProductList;

    @Inject
    MainStore(RxFlux rxFlux) {
        super(rxFlux.getDispatcher());
    }

    @Override
    @Subscribe()
    public void onRxAction(RxAction rxAction) {
        switch (rxAction.getType()) {
            case MainAction.GET_GIT_REPO_LIST:
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
