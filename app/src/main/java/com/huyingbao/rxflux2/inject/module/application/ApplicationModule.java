package com.huyingbao.rxflux2.inject.module.application;

import android.app.Application;
import android.content.Context;

import com.huyingbao.rxflux2.RxFlux;
import com.huyingbao.rxflux2.inject.qualifier.ContextLife;
import com.huyingbao.rxflux2.store.AppStore;
import com.huyingbao.rxflux2.util.LocalStorageUtils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Module是一个依赖的制造工厂
 * Created by liujunfeng on 2017/1/1.
 */
@Module(includes = {HttpApiModule.class, FluxActionModule.class})
public class ApplicationModule {
    Application mApplication;

    /**
     * 带有参数的 module 构造方法,必须显式的调用生成实例对象
     *
     * @param application
     */
    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides//添加@Singleton标明该方法产生只产生一个实例
    @Singleton
    @ContextLife("Application")
    public Context provideContext() {
        return mApplication.getApplicationContext();
    }

    @Provides
    @Singleton
    public LocalStorageUtils provideLocalStorageUtils() {
        return LocalStorageUtils.getInstance();
    }

    @Provides
    @Singleton
    public AppStore provideAppStore(RxFlux rxFlux) {
        return AppStore.getInstance(rxFlux.getDispatcher());
    }
}
