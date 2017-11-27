package com.huyingbao.rxflux2.inject.module;

import android.app.Application;
import android.content.Context;

import com.huyingbao.rxflux2.RxFlux;
import com.huyingbao.rxflux2.action.ActionCreator;
import com.huyingbao.rxflux2.action.TestActionCreator;
import com.huyingbao.rxflux2.api.HttpApi;
import com.huyingbao.rxflux2.inject.qualifier.ContextLife;
import com.huyingbao.rxflux2.store.AppStore;
import com.huyingbao.rxflux2.util.AppUtils;
import com.huyingbao.rxflux2.util.LocalStorageUtils;

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
    public LocalStorageUtils provideLocalStorageUtils() {
        return LocalStorageUtils.getInstance();
    }

    @Provides
    @Singleton
    public AppStore provideAppStore(RxFlux rxFlux) {
        return AppStore.getInstance(rxFlux.getDispatcher());
    }

    /**
     * 创建一个HybApi的实现类单例对象
     *
     * @return mHttpApi
     */
    @Provides
    @Singleton // 添加@Singleton标明该方法产生只产生一个实例
    public HttpApi provideHttpApi() {
        return Mockito.mock(HttpApi.class);
    }

    @Provides
    @Singleton
    public RxFlux provideRxFlux() {
        return RxFlux.init(AppUtils.getApplication());
    }

    /**
     * @return
     */
    @Provides
    @Singleton
    public ActionCreator provideActionCreator() {
        return Mockito.mock(TestActionCreator.class);
    }
}
