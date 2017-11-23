package com.huyingbao.rxflux2.inject.module;

import android.app.Application;
import android.content.Context;

import com.huyingbao.rxflux2.RxFlux;
import com.huyingbao.rxflux2.util.LogLevel;
import com.huyingbao.rxflux2.BuildConfig;
import com.huyingbao.rxflux2.androidtest.action.TestActionCreatorImpl;
import com.huyingbao.rxflux2.androidtest.api.TestHttpApi;
import com.huyingbao.rxflux2.inject.qualifier.ContextLife;
import com.huyingbao.rxflux2.action.ActionCreator;
import com.huyingbao.rxflux2.net.HttpApi;
import com.huyingbao.rxflux2.stores.BaseHttpStore;
import com.huyingbao.rxflux2.stores.BaseStore;
import com.huyingbao.rxflux2.utils.AppUtils;
import com.huyingbao.rxflux2.utils.SharedPrefUtils;

import org.mockito.Mockito;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Module是一个依赖的制造工厂
 * Created by liujunfeng on 2017/1/1.
 */
@Module
public class TestApplicationModule {
    Application mApplication;

    /**
     * 带有参数的 module 构造方法,必须显式的调用生成实例对象
     *
     * @param application
     */
    public TestApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides // 添加@Singleton标明该方法产生只产生一个实例
    @Singleton
    @ContextLife("Application")
    public Context provideContext() {
        return mApplication.getApplicationContext();
    }

    @Provides
    @Singleton
    public SharedPrefUtils provideLocalStorageUtils() {
        return SharedPrefUtils.getCommonSP();
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

    /**
     * 创建一个HybApi的实现类单例对象
     *
     * @return mHttpApi
     */
    @Provides
    @Singleton // 添加@Singleton标明该方法产生只产生一个实例
    public HttpApi provideHttpApi() {
        return Mockito.mock(TestHttpApi.class);
    }

    @Provides
    @Singleton
    public RxFlux provideRxFlux() {
        RxFlux.LOG_LEVEL = BuildConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE;
        return RxFlux.init(AppUtils.getApplication());
    }

    /**
     * @return
     */
    @Provides
    @Singleton
    public ActionCreator provideActionCreator() {
        return Mockito.mock(TestActionCreatorImpl.class);
    }
}
