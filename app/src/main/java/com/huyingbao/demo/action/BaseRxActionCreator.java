package com.huyingbao.demo.action;

import android.content.Context;
import android.support.annotation.NonNull;

import com.huyingbao.demo.api.HttpApi;
import com.huyingbao.demo.constant.ActionsKeys;
import com.huyingbao.demo.util.LocalStorageUtils;
import com.huyingbao.demo.widget.dialog.LoadingDialog;
import com.huyingbao.rxflux2.action.RxAction;
import com.huyingbao.rxflux2.action.RxActionCreator;
import com.huyingbao.rxflux2.dispatcher.Dispatcher;
import com.huyingbao.rxflux2.util.DisposableManager;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * action创建发送管理类
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
     * 发送网络action 不显示进度框,验证返回数据session是否过期(大部分接口调用)
     *
     * @param action
     * @param httpObservable
     */
    protected <T> void postHttpAction(RxAction action, Observable<T> httpObservable) {
        this.postHttpActionNoCheck(action, httpObservable.flatMap(verifyResponse()).retryWhen(retryLogin()));
    }

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
     * 发送action 显示进度框,验证返回数据是否正确
     *
     * @param context
     * @param action
     * @param httpObservable
     */
    protected <T> void postLoadingHttpAction(Context context, RxAction action, Observable<T> httpObservable) {
        this.postLoadingHttpActionNoCheck(context, action, httpObservable.flatMap(verifyResponse()).retryWhen(retryLogin()));
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

    // region 功能方法Function
    /**
     * 验证接口返回数据是正常
     */
    @NonNull
    private <T> Function<T, Observable<T>> verifyResponse() {
        return response -> {
            //没有数据,返回服务器异常
//            if (response == null || !(response instanceof HttpResponse))
//                return Observable.error(new RxHttpException(Constants.ERROR_SERVER, "服务器异常"));
//            //有数据,code不是成功码,返回自定义异常
//            if (((HttpResponse) response).getReturnCode() != Constants.SUCCESS_CODE)
//                return Observable.error(new RxHttpException((HttpResponse) response));
            //数据正常,返回正常数据
            return Observable.just(response);
        };
    }

    /**
     * 重复三次操作
     */
    @NonNull
    private Function<Observable<? extends Throwable>, Observable<?>> retryLogin() {
        return observable -> observable
//                .flatMap(throwable -> {
//                    //网络连接失败，切换网络
//                    if (throwable instanceof SocketException || throwable instanceof SocketTimeoutException) {
//                        //切换另一个网络，并重试!
//                        ServerUtils.setServerState(!ServerUtils.getServerState());
//                        return mHttpApi.connectServer();
//                    }
//                    //不是自定义异常,直接返回异常信息,UI会展示
//                    if (!(throwable instanceof RxHttpException))
//                        return Observable.error(throwable);
//                    //不是自定义异常中的session过期,直接返回异常信息,UI会展示
//                    if (((RxHttpException) throwable).code() != Constants.ERROR_SESSION_TIMEOUT)
//                        return Observable.error(throwable);
//                    String name = mLocalStorageUtils.getString(ActionsKeys.NAME, null);
//                    String pwd = mLocalStorageUtils.getString(ActionsKeys.PWD, null);
//                    //没有登录账号或者密码无法重新登录,直接返回异常信息,UI会展示
//                    if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pwd))
//                        return Observable.error(throwable);
//                    //重新登录
//                    RxAction rxAction = newRxAction(Actions.LOGIN,
//                            ActionsKeys.NAME, name,
//                            ActionsKeys.PWD, pwd,
//                            ActionsKeys.CHANNEL_ID, mLocalStorageUtils.getString(ActionsKeys.CHANNEL_ID, ""));
//                    return mHttpApi.login(rxAction.getData()).flatMap(loginHttpResponse -> {
//                        if (TextUtils.equals(loginHttpResponse.getReturnCode(), Constants.SUCCESS_CODE))
//                            return Observable.just(loginHttpResponse);
//                        return Observable.error(throwable);
//                })
                .zipWith(Observable.range(1, 3), (throwable, i) -> i)
                .flatMap(retryCount -> Observable.timer((long) Math.pow(1, retryCount), TimeUnit.SECONDS));
    }
    // endregion
}
