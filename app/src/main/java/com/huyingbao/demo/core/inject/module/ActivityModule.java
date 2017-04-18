package com.huyingbao.demo.core.inject.module;

import android.app.Activity;
import android.content.Context;

import com.hardsoftstudio.rxflux.RxFlux;
import com.huyingbao.demo.BuildConfig;
import com.huyingbao.demo.core.inject.qualifier.ContextLife;
import com.huyingbao.demo.core.inject.scope.PerActivity;
import com.huyingbao.demo.stores.FileStore;
import com.tbruyelle.rxpermissions.RxPermissions;

import dagger.Module;
import dagger.Provides;

/**
 * activity中的依赖制造类
 * Module是一个依赖的制造工厂
 * 保存在ActivityComponent
 * Created by Liu Junfeng on 2017/1/1.
 */
@Module
public class ActivityModule {
    private Activity mActivity;

    public ActivityModule(Activity activity) {
        mActivity = activity;
    }

    /**
     * 通过自定义的@ContextLife区分返回类型相同的@Provides 方法
     *
     * @return
     */
    @Provides
    @PerActivity
    @ContextLife("Activity")
    public Context provideContext() {
        return mActivity;
    }

    @Provides
    @PerActivity
    public Activity provideActivity() {
        return mActivity;
    }

    @Provides
    @PerActivity
    public FragmentModule provideFragmentModule() {
        return new FragmentModule();
    }

    @Provides
    @PerActivity
    public RxPermissions provideRxPermissions() {
        RxPermissions rxPermissions = new RxPermissions(mActivity);
        rxPermissions.setLogging(BuildConfig.DEBUG);
        return rxPermissions;
    }


    @Provides
    @PerActivity
    public FileStore provideFileStore(RxFlux rxFlux) {
        return new FileStore(rxFlux.getDispatcher());
    }
}
