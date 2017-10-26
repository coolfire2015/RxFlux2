package com.huyingbao.rxflux2.base.application;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.huyingbao.rxflux2.inject.component.ApplicationComponent;
import com.huyingbao.rxflux2.inject.component.DaggerApplicationComponent;
import com.huyingbao.rxflux2.inject.module.application.ApplicationModule;
import com.huyingbao.rxflux2.store.AppStore;
import com.huyingbao.rxflux2.util.AppUtils;

import javax.inject.Inject;

/**
 * Application multidex分包 依赖注入 初始化注释
 * Created by liujunfeng on 2017/1/1.
 */
public class BaseApplication extends Application{
    @Inject
    AppStore mAppStore;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);// multidex分包
    }


    @Override
    public void onCreate() {
        super.onCreate();
        // 保存application实例对象
        AppUtils.setApplication(this);
        //初始化dagger
        initDagger();
        // 依赖注入
        AppUtils.getApplicationComponent().inject(this);
        // 注册全局store
        mAppStore.register();
    }

    /**
     * 重置依赖注入.
     */
    private void initDagger() {
        // Module实例的创建
        // 如果Module只有有参构造器,则必须显式传入Module实例,
        // 单例的有效范围随着其依附的Component,
        // 为了使得@Singleton的作用范围是整个Application,需要添加以下代码
        ApplicationComponent applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this)).build();
        AppUtils.setApplicationComponent(applicationComponent);
    }
}
