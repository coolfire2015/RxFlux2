package com.huyingbao.demo.inject.module.application;

import android.app.Application;
import android.content.Context;

import com.hardsoftstudio.rxflux.RxFlux;
import com.huyingbao.demo.inject.qualifier.ContextLife;
import com.huyingbao.demo.stores.BaseHttpStore;
import com.huyingbao.demo.stores.BaseStore;
import com.huyingbao.demo.util.LocalStorageUtils;

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
        return LocalStorageUtils.getInstance(mApplication.getApplicationContext());
    }

    @Provides
    @Singleton
    public BaseStore provideBaseStore(RxFlux rxFlux) {
        return new BaseStore(rxFlux.getDispatcher());
    }

    @Provides
    @Singleton
    public BaseHttpStore provideBaseHttpStore(RxFlux rxFlux) {
        return new BaseHttpStore(rxFlux.getDispatcher());
    }
}
