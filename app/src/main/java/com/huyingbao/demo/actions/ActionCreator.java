package com.huyingbao.demo.actions;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hardsoftstudio.rxflux.action.RxAction;
import com.hardsoftstudio.rxflux.action.RxActionCreator;
import com.hardsoftstudio.rxflux.dispatcher.Dispatcher;
import com.hardsoftstudio.rxflux.util.SubscriptionManager;
import com.huyingbao.demo.core.api.HttpApi;
import com.huyingbao.demo.inject.component.ApplicationComponent;
import com.huyingbao.demo.utils.LocalStorageUtils;
import com.huyingbao.demo.widgets.dialog.LoadingDialog;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * action创建发送管理类
 * Created by Liu Junfeng on 2017/1/1.
 */
public class ActionCreator extends RxActionCreator {
    @Inject
    HttpApi mHttpApi;
    @Inject
    LocalStorageUtils mLocalStorageUtils;

    private LoadingDialog mLoadingDialog;//进度条

    /**
     * 构造方法,传入dispatcher和订阅管理器
     *
     * @param dispatcher
     * @param manager
     */
    public ActionCreator(Dispatcher dispatcher, SubscriptionManager manager) {
        super(dispatcher, manager);
        ApplicationComponent.Instance.get().inject(this);
    }

    // TODO 发送action

    /**
     * 发送本地action
     *
     * @param action
     */
    public void postBaseAction(RxAction action) {
        postRxAction(action);
    }

    /**
     * 发送网络action
     * 不显示进度框,验证返回数据session是否过期(大部分接口调用)
     *
     * @param action
     * @param httpObservable
     */
    public <T> void postHttpAction(RxAction action, Observable<T> httpObservable) {
        if (hasRxAction(action)) return;
        addRxAction(action, getSubscribe(action, getHttpResponseCheckRetry(httpObservable)));
    }

    /**
     * 发送action
     * 显示进度框,验证返回数据session是否过期
     *
     * @param context
     * @param action
     * @param httpObservable
     */
    public <T> void postLoadingHttpAction(Context context, RxAction action, Observable<T> httpObservable) {
        this.postLoadingHttpActionNoCheck(context, action, getHttpResponseCheckRetry(httpObservable));
    }

    /**
     * 发送action
     * 显示进度框,验证返回数据session是否过期
     *
     * @param context
     * @param action
     * @param httpObservable
     */
    public <T> void postLoadingHttpActionNoCheck(Context context, RxAction action, Observable<T> httpObservable) {
        if (hasRxAction(action)) return;
        addRxAction(action, getLoadingSubscribe(context, false, action, httpObservable));
    }

    //TODO 进行订阅Subscription

    /**
     * 调用网络接口,传入接口自己的回调(非RxFlux模式接口,无法发送接口数据,eg:新闻模块获取新闻列表接口)调用接口,发送接口返回数据
     *
     * @param action
     * @param httpObservable
     * @return
     */
    public <T> Subscription getSubscribe(RxAction action, Observable<T> httpObservable) {
        return httpObservable//1:指定IO线程
                .subscribeOn(Schedulers.io())//1:指定IO线程
                .observeOn(AndroidSchedulers.mainThread())//2:指定主线程
                .subscribe(//2:指定主线程
                        getHttpResponseAction1(action),
                        getThrowableAction1(action));
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
    private <T> Subscription getLoadingSubscribe(Context context, boolean cancelAble, RxAction action, Observable<T> httpObservable) {
        return httpObservable//1:指定IO线程
                .subscribeOn(Schedulers.io())//1:指定IO线程
                .doOnSubscribe(() -> showLoading(context, cancelAble))//2:指定主线程
                .subscribeOn(AndroidSchedulers.mainThread())//2:在doOnSubscribe()之后，使用subscribeOn()就可以指定其运行在哪中线程。
                .observeOn(AndroidSchedulers.mainThread())//3:指定主线程
                .subscribe(//3:指定主线程
                        getHttpResponseAction1(action),
                        getThrowableLoadingAction1(action));
    }

    //TODO 回调方法Action1

    /**
     * 传递数据
     *
     * @param action
     * @return
     */
    @NonNull
    private <T> Action1<T> getHttpResponseAction1(RxAction action) {
        return richHttpResponse -> {
            dismissLoading();
            action.getData().put(ActionsKeys.RESPONSE, richHttpResponse);
            postRxAction(action);
        };
    }

    /**
     * 传递错误,取消引导框
     *
     * @param action
     * @return
     */
    @NonNull
    private Action1<Throwable> getThrowableLoadingAction1(RxAction action) {
        return throwable -> {
            dismissLoading();
            postError(action, throwable);
        };
    }

    /**
     * 传递错误
     *
     * @param action
     * @return
     */
    @NonNull
    private Action1<Throwable> getThrowableAction1(RxAction action) {
        return throwable -> postError(action, throwable);
    }

    //TODO 功能方法Func

    /**
     * 在服务器连接畅通状态下检测调用接口时,有没有session过期
     * 服务器调用错误,会跳过该检测,到retryWhen
     */
    @NonNull
    private <T> Func1<T, Observable<T>> checkSessionAble() {
        return httpResponse -> {
//            if (httpResponse == null) {
//                HttpResponse response = new HttpResponse(8001, "服务器异常,获取不到数据!");
//                return Observable.error(new HttpException(Response.error(400,httpResponse));
//            }
//            switch (httpResponse.getResponseCode()) {
//                case 8000://session 过期
//                case 9999://服务器错误
//                    return Observable.error(new RxHttpException(httpResponse));
//            }
            return Observable.just(httpResponse);
        };
    }

    /**
     * 检查登陆
     *
     * @return
     */
    private <T> Func1<T, Observable<T>> checkLoginAble() {
        return httpResponse -> {
//            if (httpResponse == null) {
//                HttpResponse response = new HttpResponse(8001, "服务器异常,获取不到数据!");
//                return Observable.error(new RxHttpException(response));
//            }
//            switch (httpResponse.getResponseCode()) {
//                case 404://用户名或密码错误
//                    return Observable.error(new RxHttpException(httpResponse));
//            }
            return Observable.just(httpResponse);
        };
    }

    /**
     * 重复三次操作
     */
    @NonNull
    private Func1<Observable<? extends Throwable>, Observable<?>> retryLogin() {
        return observable -> observable
                .flatMap(throwable -> {
                    if (throwable instanceof HttpException) {
//                int httpCode = ((RxHttpException) throwable).code();
//                if (httpCode == 8000)//session过期需要重新登录
//                {
//                    String account = LocalStorageUtils.getInstance().getString(ActionsKeys.ACCOUNT, null);
//                    String password = LocalStorageUtils.getInstance().getString(ActionsKeys.PASSWORD, null);
//                    if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password))
//                        return Observable.error(new RxHttpException(new HttpResponse(8002, "账号密码错误,请重新登录!")));
//                    return BaseApplication.getInstance().getHybApi()
//                            .login(account, StringUtils.toMD5(password), LocalStorageUtils.getInstance().getString(ActionsKeys.CHANNEL_ID, null))
//                            .flatMap(checkLoginAble());
//                }
                        return Observable.error(throwable);
                    }
                    return Observable.error(throwable);
                })
                .zipWith(Observable.range(1, 3), (throwable, i) -> i)
                .flatMap(retryCount -> Observable.timer((long) Math.pow(1, retryCount), TimeUnit.SECONDS));
    }


    /**
     * 重新登录,使用.zip() + .range()实现有限次数的重订阅
     */
    @NonNull
    private static Func1<Observable<? extends Throwable>, Observable<?>> retry() {
        return observable ->
                observable.zipWith(Observable.range(1, 3), (throwable, integer) -> integer)
                        .flatMap(retryCount -> Observable.timer((long) Math.pow(5, retryCount), TimeUnit.SECONDS));
    }

    /**
     * 重新登录
     */
    Func1<Observable<? extends Throwable>, Observable<?>> retryLogin = observable ->
            observable.flatMap(throwable -> {
                if (throwable instanceof HttpException) {
//                    int httpCode = ((HttpException) throwable).code();
//                    if (httpCode == 404) {
//                        User user = new User();
////                        user.setPhone(mLocalStorageUtils.getLoginName());
////                        user.setPassword(mLocalStorageUtils.getPassword());
////                        user.setChannelId(mLocalStorageUtils.getChannelId());
//                        user.setChannelType(3);
//                        return mHttpApi.login(user);
//                    }
//                    return Observable.error(throwable);
                }
                return Observable.error(throwable);
            });

    //TODO 进度框

    /**
     * 显示进度框
     *
     * @param context
     * @param flag
     */
    private void showLoading(Context context, boolean flag) {
        if (mLoadingDialog == null)
            mLoadingDialog = new LoadingDialog(context);
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

    //TODO 被观察者Observable

    /**
     * 获取网络接口,检测接口返回数据是否session过期,过期重新登录
     *
     * @param httpObservable
     * @return
     */
    public <T> Observable<T> getHttpResponseCheckRetry(Observable<T> httpObservable) {
        return httpObservable//1:指定io线程
                .flatMap(checkSessionAble())//2:指定io线程
                .retryWhen(retryLogin());
    }
}


