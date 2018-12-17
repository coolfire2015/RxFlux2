package com.huyingbao.rxflux2.inject.module.application;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Module是一个依赖的制造工厂，
 * 通过Singleton创建出来的单例并不保持在静态域上，
 * 而是保留在Component实例中。
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
    public Context provideContext() {
        return mApplication.getApplicationContext();
    }
}
