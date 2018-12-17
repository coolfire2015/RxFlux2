package com.huyingbao.rxflux2.base.activity;

import android.os.Bundle;

import com.huyingbao.rxflux2.dispatcher.RxViewDispatch;
import com.huyingbao.rxflux2.inject.component.ActivityComponent;
import com.huyingbao.rxflux2.inject.component.DaggerActivityComponent;
import com.huyingbao.rxflux2.inject.module.ActivityModule;
import com.huyingbao.rxflux2.util.AppUtils;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;

/**
 * 所有的activity的父类,实现RxFlux接口, 通过onRxStoreChanged接收store发送的数据
 * Created by liujunfeng on 2017/1/1.
 */
public abstract class BaseRxFluxActivity extends RxAppCompatActivity implements RxViewDispatch {
    //非静态，除了针对整个App的Component可以静态，其他一般都不能是静态的。
    protected ActivityComponent mActivityComponent;

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
        //创建之后的操作
        afterCreate(savedInstanceState);
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

    /**
     * 获取对应的activityComponent
     *
     * @return
     */
    public ActivityComponent getActivityComponent() {
        return mActivityComponent;
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
