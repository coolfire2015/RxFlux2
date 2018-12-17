package com.huyingbao.rxflux2.base.application;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.huyingbao.rxflux2.inject.component.ApplicationComponent;
import com.huyingbao.rxflux2.inject.component.DaggerApplicationComponent;
import com.huyingbao.rxflux2.inject.module.application.ApplicationModule;
import com.huyingbao.rxflux2.util.AppUtils;
import com.huyingbao.simple.BuildConfig;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.squareup.leakcanary.LeakCanary;

/**
 * Application multidex分包 依赖注入 初始化注释
 * Created by liujunfeng on 2017/1/1.
 */
public class BaseApplication extends Application {
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
        //初始化debug
        initDebug();
        // 初始化dagger
        initDagger();
        // 依赖注入
        AppUtils.getApplicationComponent().inject(this);
    }


    /**
     * 初始化debug工具
     */
    private void initDebug() {
        //.logStrategy(customLog) // (Optional) Changes the log strategy to print out. Default LogCat
        //.methodOffset(5)        // (Optional) Hides internal method calls up to offset. Default 5
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(2)         // (Optional) How many method line to show. Default 2
                .tag("DmRxFlux")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return BuildConfig.LOG_DEBUG;
            }
        });
        if (BuildConfig.LOG_DEBUG) {
            if (LeakCanary.isInAnalyzerProcess(this)) return;
            LeakCanary.install(this);
        }
    }

    /**
     * 初始化dagger
     */
    protected void initDagger() {
        // Module实例的创建， 如果Module只有有参构造器,则必须显式传入Module实例,
        ApplicationComponent applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this)).build();
        // 单例的有效范围随着其依附的Component, 为了使得@Singleton的作用范围是整个Application,需要添加以下代码
        AppUtils.setApplicationComponent(applicationComponent);
    }
}
