package com.huyingbao.rxflux2.action;

import com.huyingbao.rxflux2.api.HttpApi;
import com.huyingbao.rxflux2.constant.ActionsKeys;
import com.huyingbao.rxflux2.dispatcher.Dispatcher;
import com.huyingbao.rxflux2.util.DisposableManager;
import com.orhanobut.logger.Logger;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * rxAction创建发送管理类
 * Created by liujunfeng on 2017/1/1.
 */
public class BaseRxActionCreator extends RxActionCreator {
    @Inject
    protected HttpApi mHttpApi;

    /**
     * 构造方法,传入dispatcher和订阅管理器
     *
     * @param dispatcher
     * @param manager
     */
    public BaseRxActionCreator(Dispatcher dispatcher, DisposableManager manager) {
        super(dispatcher, manager);
    }

    /**
     * 发送网络action 不显示进度框,验证返回数据session是否过期(大部分接口调用)
     *
     * @param rxAction
     * @param httpObservable
     */
    protected <T> void postHttpAction(RxAction rxAction, Observable<T> httpObservable) {
        this.postHttpActionNoCheck(rxAction, httpObservable);
    }

    /**
     * 发送网络action 不显示进度框,不验证返回数据session是否过期
     *
     * @param rxAction
     * @param httpObservable
     */
    protected <T> void postHttpActionNoCheck(RxAction rxAction, Observable<T> httpObservable) {
        if (hasRxAction(rxAction)) return;
        addRxAction(rxAction, getDisposable(rxAction, httpObservable));
    }

     /**
     * 调用网络接口,传入接口自己的回调(非RxFlux模式接口,无法发送接口数据,eg:新闻模块获取新闻列表接口)调用接口,发送接口返回数据
     *
     * @param rxAction
     * @param httpObservable
     * @return
     */
    private <T> Disposable getDisposable(RxAction rxAction, Observable<T> httpObservable) {
        return httpObservable// 1:指定IO线程
                .subscribeOn(Schedulers.io())// 1:指定IO线程
                .observeOn(AndroidSchedulers.mainThread())// 2:指定主线程
                .subscribe(// 2:指定主线程
                        richHttpResponse -> {
                            rxAction.getData().put(ActionsKeys.RESPONSE, richHttpResponse);
                            postRxAction(rxAction);
                        },
                        throwable -> {
                            Logger.e("Action Type = " + rxAction.getType()
                                    + "\nError Class = " + throwable.getClass().getSimpleName()
                                    + "\nError Message = " + throwable.getMessage());
                            postError(rxAction, throwable);
                        }
                );
    }
}
