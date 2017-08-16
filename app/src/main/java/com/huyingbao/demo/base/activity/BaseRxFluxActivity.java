package com.huyingbao.demo.base.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDelegate;
import android.view.MenuItem;
import android.widget.Toast;

import com.hardsoftstudio.rxflux.action.RxError;
import com.hardsoftstudio.rxflux.dispatcher.RxViewDispatch;
import com.huyingbao.demo.action.ActionCreator;
import com.huyingbao.demo.api.HttpApi;
import com.huyingbao.demo.inject.component.ActivityComponent;
import com.huyingbao.demo.inject.component.DaggerActivityComponent;
import com.huyingbao.demo.inject.module.ActivityModule;
import com.huyingbao.demo.inject.qualifier.ContextLife;
import com.huyingbao.demo.store.BaseHttpStore;
import com.huyingbao.demo.store.BaseStore;
import com.huyingbao.demo.util.AppUtils;
import com.huyingbao.demo.util.LocalStorageUtils;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.net.SocketException;
import java.net.SocketTimeoutException;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * 所有的activity的父类,实现RxFlux接口, 通过onRxStoreChanged接收store发送的数据
 * Created by liujunfeng on 2017/1/1.
 */
public abstract class BaseRxFluxActivity extends RxAppCompatActivity implements RxViewDispatch {
    //region 参数
    static {
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
    protected HttpApi mHttpApi;
    @Inject
    protected BaseStore mBaseStore;
    @Inject
    protected BaseHttpStore mBaseHttpStore;

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
        mBaseStore.register();
        mBaseHttpStore.register();
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
        Throwable throwable = error.getThrowable();
        if (throwable instanceof retrofit2.HttpException) {
            showShortToast(((retrofit2.HttpException) throwable).code() + ":服务器问题");
        } else if (throwable instanceof SocketException) {
            showShortToast("连接网络失败!");
        } else if (throwable instanceof SocketTimeoutException) {
            showShortToast("连接超时!");
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
