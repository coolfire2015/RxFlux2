package com.huyingbao.rxflux2.action;

import android.content.Context;

import com.huyingbao.rxflux2.api.HttpApi;
import com.huyingbao.rxflux2.constant.ActionsKeys;
import com.huyingbao.rxflux2.dispatcher.Dispatcher;
import com.huyingbao.rxflux2.util.DisposableManager;
import com.huyingbao.rxflux2.util.LocalStorageUtils;
import com.huyingbao.rxflux2.widget.dialog.LoadingDialog;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * rxAction创建发送管理类
 * Created by liujunfeng on 2017/1/1.
 */
class BaseRxActionCreator extends RxActionCreator {
    //region 参数
    @Inject
    protected LocalStorageUtils mLocalStorageUtils;
    @Inject
    protected HttpApi mHttpApi;
    private LoadingDialog mLoadingDialog;
    //endregion

    // region 构造方法
    /**
     * 构造方法,传入dispatcher和订阅管理器
     *
     * @param dispatcher
     * @param manager
     */
    public BaseRxActionCreator(Dispatcher dispatcher, DisposableManager manager) {
        super(dispatcher, manager);
    }
    // endregion

    // region 进度框
    /**
     * 显示进度框
     *
     * @param context
     * @param flag
     */
    private void showLoading(Context context, boolean flag) {
        if (mLoadingDialog == null) mLoadingDialog = new LoadingDialog(context);
        mLoadingDialog.setCancelable(flag);
        mLoadingDialog.show();
    }

    /**
     * 隐藏进度框
     */
    private void dismissLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }
    // endregion

    // region 发送action
    /**
     * 发送网络action 不显示进度框,不验证返回数据session是否过期
     *
     * @param action
     * @param httpObservable
     */
    protected <T> void postHttpActionNoCheck(RxAction action, Observable<T> httpObservable) {
        if (hasRxAction(action)) return;
        addRxAction(action, getDisposable(action, httpObservable));
    }

    /**
     * 发送action 显示进度框,不验证返回数据是否正确
     *
     * @param context
     * @param action
     * @param httpObservable
     */
    protected <T> void postLoadingHttpActionNoCheck(Context context, RxAction action, Observable<T> httpObservable) {
        if (hasRxAction(action)) return;
        addRxAction(action, getLoadingDisposable(context, false, action, httpObservable));
    }
    // endregion

    // region 进行订阅，并获取订阅之后的订阅关系Disposable
    /**
     * 调用网络接口,传入接口自己的回调(非RxFlux模式接口,无法发送接口数据,eg:新闻模块获取新闻列表接口)调用接口,发送接口返回数据
     *
     * @param action
     * @param httpObservable
     * @return
     */
    private <T> Disposable getDisposable(RxAction action, Observable<T> httpObservable) {
        return httpObservable// 1:指定IO线程
                .subscribeOn(Schedulers.io())// 1:指定IO线程
                .observeOn(AndroidSchedulers.mainThread())// 2:指定主线程
                .subscribe(// 2:指定主线程
                        richHttpResponse -> {
                            dismissLoading();
                            action.getData().put(ActionsKeys.RESPONSE, richHttpResponse);
                            postRxAction(action);
                        },
                        throwable -> postError(action, throwable)
                );
    }

    /**
     * 调用接口,发送接口返回数据,增加进度框
     *
     * @param context
     * @param cancelAble
     * @param action
     * @param httpObservable 被观察者,一般是获取网络数据
     * @return
     */
    private <T> Disposable getLoadingDisposable(Context context, boolean cancelAble, RxAction action, Observable<T> httpObservable) {
        return httpObservable// 1:指定IO线程
                .subscribeOn(Schedulers.io())// 1:指定IO线程
                .doOnSubscribe(subscription -> showLoading(context, cancelAble))// 2:指定主线程
                .subscribeOn(AndroidSchedulers.mainThread())// 2:在doOnSubscribe()之后，使用subscribeOn()就可以指定其运行在哪中线程。
                .observeOn(AndroidSchedulers.mainThread())// 3:指定主线程
                .subscribe(// 3:指定主线程
                        richHttpResponse -> {
                            dismissLoading();
                            action.getData().put(ActionsKeys.RESPONSE, richHttpResponse);
                            postRxAction(action);
                        },
                        throwable -> {
                            dismissLoading();
                            postError(action, throwable);
                        }
                );
    }
    // endregion
}
