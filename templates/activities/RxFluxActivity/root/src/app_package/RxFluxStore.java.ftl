package ${packageName}.store;

import com.huyingbao.rxflux2.action.RxAction;
import com.huyingbao.rxflux2.dispatcher.Dispatcher;
import com.huyingbao.rxflux2.store.RxStore;
import com.huyingbao.rxflux2.store.RxStoreChange;

public class ${storeClass} extends RxStore {
    public ${storeClass}(Dispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void onRxAction(RxAction rxAction) {
        switch (rxAction.getType()) {
            case "default":
                break;
            default:
                return;
        }
        postChange(new RxStoreChange(getClass().getSimpleName(), rxAction));
    }
}
