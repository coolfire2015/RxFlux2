package com.huyingbao.rxflux2.store;

import com.huyingbao.rxflux2.action.RxAction;
import com.huyingbao.rxflux2.dispatcher.Dispatcher;

/**
 * 存在于 BaseApplication 的 mApplicationComponent 中 全局
 * Created by liujunfeng on 2017/1/1.
 */
public class AppStore extends RxStore {
    private static AppStore sInstance;

    private AppStore(Dispatcher dispatcher) {
        super(dispatcher);
    }

    public static AppStore getInstance(Dispatcher dispatcher) {
        if (sInstance == null) sInstance = new AppStore(dispatcher);
        return sInstance;
    }

    /**
     * This callback will get all the rxActions, each store must react on the types he want and do
     * some logic with the model, for example add it to the list to cache it, modify
     * fields etc.. all the logic for the models should go here and then call postChange so the
     * view request the new data
     * 这个回调接收所有的actions(RxAction),每个store都必须根据action的type做出反应,,例如将其添加到列表缓存,修改字段等
     * 所有的逻辑模型应该在这里,然后调用postChange请求新数据视图
     */
    @Override
    public void onRxAction(RxAction rxAction) {
        switch (rxAction.getType()) {
            case "Debug":
                break;
            default://必须有,接收到非自己处理的action返回
                return;
        }
        postChange(new RxStoreChange(getClass().getSimpleName(), rxAction));//只发送自己处理的action
    }
}
