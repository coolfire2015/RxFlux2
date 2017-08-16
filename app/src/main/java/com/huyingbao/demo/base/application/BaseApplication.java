package com.huyingbao.demo.base.application;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.huyingbao.demo.BuildConfig;
import com.huyingbao.demo.inject.component.ApplicationComponent;
import com.huyingbao.demo.inject.component.DaggerApplicationComponent;
import com.huyingbao.demo.inject.module.application.ApplicationModule;
import com.huyingbao.demo.store.BaseHttpStore;
import com.huyingbao.demo.store.BaseStore;
import com.huyingbao.demo.util.AppUtils;
import com.huyingbao.demo.util.CommonUtils;
import com.huyingbao.demo.util.DevUtils;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.pgyersdk.crash.PgyCrashManager;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.squareup.leakcanary.LeakCanary;
import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixManager;

import javax.inject.Inject;

/**
 * Application multidex分包 依赖注入 初始化注释
 * Created by liujunfeng on 2017/1/1.
 */
public class BaseApplication extends Application {
    @Inject
    BaseStore mBaseStore;
    @Inject
    BaseHttpStore mBaseHttpStore;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);//multidex分包
    }

    /**
     * 内存泄漏检测
     * LeakCanary.install(this);
     * 初始化 flowmanager
     * FlowManager.init(new FlowConfig.Builder(this).build());
     */
    @Override
    public void onCreate() {
        super.onCreate();
        // 保存application实例对象
        AppUtils.setApplication(this);
        //初始化蒲公英异常捕获
        PgyCrashManager.register(this);
        //初始化hotfix
        initHotfix();
        //初始化debug
        initDebug();
        //初始化dagger
        initDagger();
        // 依赖注入
        AppUtils.getApplicationComponent().inject(this);
        // 注册全局store
        mBaseStore.register();
        //注册全局store
        mBaseHttpStore.register();
        //初始化数据库
        FlowManager.init(this);
        //初始化flowup
        //initFlowUp();
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

    /**
     * 初始化hotfix
     */
    private void initHotfix() {
        SophixManager.getInstance().setContext(this)
                .setAppVersion(DevUtils.getAppVerson(this))
                .setAesKey(null)
                .setEnableDebug(BuildConfig.LOG_DEBUG)
                .setPatchLoadStatusStub((mode, code, info, handlePatchVersion) -> {
                    //补丁加载回调通知
                    switch (code) {
                        case PatchStatus.CODE_LOAD_SUCCESS://表明补丁加载成功
                            Logger.d("表明补丁加载成功");
                            break;
                        case PatchStatus.CODE_LOAD_RELAUNCH://表明新补丁生效需要重启. 开发者可提示用户或者强制重启; 建议: 用户可以监听进入后台事件, 然后应用自杀
                            Logger.d("表明新补丁生效需要重启");
                            if (!CommonUtils.isTopActivity(this) || !CommonUtils.isVisible(this))
                                android.os.Process.killProcess(android.os.Process.myPid());
                            break;
                        case PatchStatus.CODE_LOAD_FAIL://内部引擎异常, 推荐此时清空本地补丁, 防止失败补丁重复加载
                            Logger.d("内部引擎异常");
                            SophixManager.getInstance().cleanPatches();
                            break;
                        default:// 其它错误信息, 查看PatchStatus类说明
                            Logger.d("hotfix：" + code + "\n" + info);
                            break;
                    }
                })
                .initialize();
    }

    /**
     * 初始化FlowUp
     */
//    private void initFlowUp() {
//        FlowUp.Builder.with(this)
//                .apiKey("09f13da11053459580446728cfca2f06")
//                .forceReports(true)
//                .start();
//    }
}