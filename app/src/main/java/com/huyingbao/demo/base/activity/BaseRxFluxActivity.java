package com.huyingbao.demo.base.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.hardsoftstudio.rxflux.action.RxAction;
import com.hardsoftstudio.rxflux.action.RxError;
import com.hardsoftstudio.rxflux.dispatcher.RxViewDispatch;
import com.huyingbao.demo.R;
import com.huyingbao.demo.core.actions.ActionCreator;
import com.huyingbao.demo.core.actions.BaseActions;
import com.huyingbao.demo.core.api.HttpApi;
import com.huyingbao.demo.core.inject.component.DaggerActivityComponent;
import com.huyingbao.demo.inject.component.ActivityComponent;
import com.huyingbao.demo.inject.component.ApplicationComponent;
import com.huyingbao.demo.inject.module.ActivityModule;
import com.huyingbao.demo.inject.qualifier.ContextLife;
import com.huyingbao.demo.stores.base.BaseHttpStore;
import com.huyingbao.demo.stores.base.BaseStore;
import com.huyingbao.demo.utils.LocalStorageUtils;

import java.io.IOException;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * 所有的activity的父类,实现RxFlux接口
 * 通过onRxStoreChanged接收store发送的数据
 * Created by Liu Junfeng on 2017/1/1.
 */
public abstract class BaseRxFluxActivity extends AppCompatActivity implements RxViewDispatch {
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

    protected String mAction;
    protected FragmentManager mFragmentManager;
    protected ActivityComponent mActivityComponent;

    public ActivityComponent getActivityComponent() {
        return mActivityComponent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //获取action
        mAction = getIntent().getStringExtra(BaseActions.BASE_ACTION);
        //获取mFragmentManager
        mFragmentManager = getSupportFragmentManager();
        //依赖注入
        inject();
        //需要在onCrate之前先注入对象
        super.onCreate(savedInstanceState);
        //设置view
        setContentView(getLayoutId());
        //绑定view
        ButterKnife.bind(this);
        //注册全局store
        if (mBaseStore == null || mBaseHttpStore == null) this.initInjector();
        mBaseStore.register();
        mBaseHttpStore.register();
        //创建之后的操作
        afterCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:// 点击返回图标事件
                if (mFragmentManager.getBackStackEntryCount() > 0) {
                    mFragmentManager.popBackStack();
                    return true;
                }
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * 启动activity
     *
     * @param cls
     */
    protected void startActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    /**
     * 显示短暂的Toast
     *
     * @param text
     */
    protected void showShortToast(String text) {
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * activity中快速创建action的方法
     *
     * @param actionId
     * @param data
     * @return
     */
    protected RxAction creatAction(@NonNull String actionId, @NonNull Object... data) {
        return mActionCreator.newRxAction(actionId, data);
    }

    /**
     * 依赖注入
     */
    protected void inject() {
        //初始化注入器
        mActivityComponent = DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(ApplicationComponent.Instance.get())
                .build();
        //注入Injector
        initInjector();
    }

    /**
     * 获取fragment事务
     *
     * @return
     */
    protected FragmentTransaction getFragmentTransaction() {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        Fragment fragment = mFragmentManager.findFragmentById(R.id.fl_content);
        if (fragment != null)
            fragmentTransaction.addToBackStack(fragment.getClass().getName()).hide(fragment);
        return fragmentTransaction;
    }

    @Override
    public void onRxError(@NonNull RxError error) {
        String stringInfo;
        Throwable throwable = error.getThrowable();
        if (throwable instanceof retrofit2.HttpException) {
            try {
                stringInfo = ((retrofit2.HttpException) throwable).response().errorBody().string();
            } catch (IOException e) {
                stringInfo = throwable.toString();
            }
        } else {
            stringInfo = throwable.toString();
        }
        showShortToast(stringInfo);
    }

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

}
