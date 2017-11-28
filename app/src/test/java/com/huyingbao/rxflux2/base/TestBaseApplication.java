package com.huyingbao.rxflux2.base;

import com.huyingbao.rxflux2.base.application.BaseApplication;
import com.huyingbao.rxflux2.inject.component.ApplicationComponent;
import com.huyingbao.rxflux2.inject.component.DaggerApplicationComponent;
import com.huyingbao.rxflux2.module.TestApplicationModule;
import com.huyingbao.rxflux2.util.AppUtils;

/**
 * Application multidex分包 依赖注入 初始化注释
 * Created by liujunfeng on 2017/1/1.
 */
public class TestBaseApplication extends BaseApplication {
    @Override
    public void onCreate() {
        // 保存application实例对象
        AppUtils.setApplication(this);
        // 初始化dagger
        initDagger();
        // 依赖注入
        AppUtils.getApplicationComponent().inject(this);
        // 注册全局store
        mAppStore.register();
    }

    /**
     * 初始化dagger
     */
    @Override
    protected void initDagger() {
        ApplicationComponent applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new TestApplicationModule(this)).build();
        AppUtils.setApplicationComponent(applicationComponent);
    }

}
