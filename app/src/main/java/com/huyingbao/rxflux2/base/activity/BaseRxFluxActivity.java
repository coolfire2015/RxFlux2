package com.huyingbao.rxflux2.base.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDelegate;
import android.util.MalformedJsonException;
import android.view.MenuItem;
import android.widget.Toast;

import com.huyingbao.rxflux2.action.ActionCreator;
import com.huyingbao.rxflux2.action.RxError;
import com.huyingbao.rxflux2.dispatcher.RxViewDispatch;
import com.huyingbao.rxflux2.inject.component.ActivityComponent;
import com.huyingbao.rxflux2.inject.component.DaggerActivityComponent;
import com.huyingbao.rxflux2.inject.module.ActivityModule;
import com.huyingbao.rxflux2.inject.qualifier.ContextLife;
import com.huyingbao.rxflux2.model.RxHttpException;
import com.huyingbao.rxflux2.store.AppStore;
import com.huyingbao.rxflux2.util.AppUtils;
import com.huyingbao.rxflux2.util.LocalStorageUtils;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * 所有的activity的父类,实现RxFlux接口, 通过onRxStoreChanged接收store发送的数据
 * Created by liujunfeng on 2017/1/1.
 */
public abstract class BaseRxFluxActivity extends RxAppCompatActivity implements RxViewDispatch {
    //region 参数
    static {//Vector使用
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Inject
    @ContextLife("Activity")
    protected Context mContext;
    @Inject
    protected ActionCreator mActionCreator;
    @Inject
    protected LocalStorageUtils mLocalStorageUtils;
    @Inject
    protected AppStore mAppStore;

    protected ActivityComponent mActivityComponent;
    //endregion

    //region 复写的方法
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 依赖注入
        inject();
        //需要在onCrate之前先注入对象
        super.onCreate(savedInstanceState);
        //设置view
        setContentView(getLayoutId());
        //绑定view
        ButterKnife.bind(this);
        //注册全局store
        mAppStore.register();
        //创建之后的操作
        afterCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:// 点击返回图标事件
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                    return true;
                }
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * 该方法不经过store,由activity直接处理
     * rxflux中对错误的处理
     */
    @Override
    public void onRxError(@NonNull RxError error) {
        switch (error.getAction().getType()) {
            default:
                handleThrowable(error);
                break;
        }
    }

    //endregion

    //region 子类和外部可以调用的方法

    /**
     * 获取对应的activityComponent
     *
     * @return
     */
    public ActivityComponent getActivityComponent() {
        return mActivityComponent;
    }

    /**
     * 显示短暂的Toast
     *
     * @param text
     */
    protected void showShortToast(String text) {
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }
    //endregion

    //region 私有方法
    private void handleThrowable(@NonNull RxError error) {
        Throwable throwable = error.getThrowable();
        // 自定义异常
        if (throwable instanceof RxHttpException) {
            String message = ((RxHttpException) throwable).message();
            showShortToast(message);
        } else if (throwable instanceof retrofit2.HttpException) {
            showShortToast(((retrofit2.HttpException) throwable).code() + ":服务器问题");
        } else if (throwable instanceof SocketException) {
            showShortToast("连接服务器失败，请检查网络状态和服务器地址配置！");
        } else if (throwable instanceof SocketTimeoutException) {
            showShortToast("连接服务器超时，请检查网络状态和服务器地址配置！");
        } else if (throwable instanceof UnknownHostException) {
            showShortToast("请输入正确的服务器地址！");
        } else if (throwable instanceof MalformedJsonException) {
            showShortToast("请检查网络状态！");
        } else {
            showShortToast(throwable == null ? "未知错误" : throwable.toString());
        }
    }

    /**
     * 依赖注入
     */
    private void inject() {
        // 初始化注入器
        mActivityComponent = DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(AppUtils.getApplicationComponent())
                .build();
        // 注入Injector
        initInjector();
    }
    //endregion

    //region 抽象方法

    /**
     * 注入Injector
     */
    public abstract void initInjector();

    /**
     * 设置view
     *
     * @return
     */
    public abstract int getLayoutId();

    /**
     * 创建之后的操作
     *
     * @param savedInstanceState
     */
    public abstract void afterCreate(Bundle savedInstanceState);
    //endregion
}
