package com.huyingbao.demo.base.application;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.hardsoftstudio.rxflux.RxFlux;
import com.huyingbao.demo.BuildConfig;
import com.huyingbao.demo.core.actions.ActionCreator;
import com.huyingbao.demo.core.api.HttpApi;
import com.huyingbao.demo.core.inject.component.DaggerApplicationComponent;
import com.huyingbao.demo.inject.component.ApplicationComponent;
import com.huyingbao.demo.inject.module.application.ApplicationModule;
import com.huyingbao.demo.stores.base.BaseHttpStore;
import com.huyingbao.demo.stores.base.BaseStore;
import com.huyingbao.demo.utils.LocalStorageUtils;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.squareup.leakcanary.LeakCanary;

import javax.inject.Inject;

/**
 * Application multidex分包 依赖注入 初始化注释 Created by liujunfeng on 2017/1/1.
 */
public class BaseApplication extends Application {
    @Inject
    RxFlux mRxFlux;

    @Inject
    ActionCreator mActionCreator;

    @Inject
    LocalStorageUtils mLocalStorageUtils;

    @Inject
    HttpApi mHttpApi;

    @Inject
    BaseStore mBaseStore;

    @Inject
    BaseHttpStore mBaseHttpStore;

    private static BaseApplication sInstance;

    public static BaseApplication getInstance() {
        if (sInstance == null)
            sInstance = new BaseApplication();
        return sInstance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);// multidex分包
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 当前实例对象
        sInstance = this;
        // 初始化dbflow
        FlowManager.init(new FlowConfig.Builder(this).build());
        // 初始化debug
        initDebug();
        // 初始化dagger
        initDagger();
        // 依赖注入
        ApplicationComponent.Instance.get().inject(this);
        // 注册全局store
        mBaseStore.register();
        // 注册全局store
        mBaseHttpStore.register();
    }

    /**
     * 初始化debug工具
     */
    private void initDebug() {
        if (BuildConfig.LOG_DEBUG) {
            Logger.init("HuYingBao").methodCount(2).hideThreadInfo().logLevel(LogLevel.FULL);
            if (LeakCanary.isInAnalyzerProcess(this))
                return;
            LeakCanary.install(this);
        } else {
            Logger.init("HuYingBao").logLevel(LogLevel.NONE);
        }
    }

    /**
     * Module实例的创建,如果Module只有有参构造器，则必须显式传入Module实例 单例的有效范围随着其依附的Component，为了使得@Singleton的作用范围是整个Application，你需要添加以下代码
     */
    private void initDagger() {
        ApplicationComponent applicationComponet =
                DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();
        ApplicationComponent.Instance.init(applicationComponet);
    }
}
// mode: 补丁模式, 0:正常请求模式 1:扫码模式 2:本地补丁模式
// code: 补丁加载状态码, 详情查看PatchStatusCode类说明
// info: 补丁加载详细说明, 详情查看PatchStatusCode类说明
// handlePatchVersion: 当前处理的补丁版本号, 0:无 -1:本地补丁 其它:后台补丁

// code: 1 补丁加载成功
// code: 6 服务端没有最新可用的补丁
// code: 11 RSASECRET错误，官网中的密钥是否正确请检查
// code: 12 当前应用已经存在一个旧补丁, 应用重启尝试加载新补丁
// code: 13 补丁加载失败, 导致的原因很多种, 比如UnsatisfiedLinkError等异常, 此时应该严格检查logcat异常日志
// code: 16 APPSECRET错误，官网中的密钥是否正确请检查
// code: 18 一键清除补丁 code: 19 连续两次queryAndLoadNewPatch()方法调用不能短于3s