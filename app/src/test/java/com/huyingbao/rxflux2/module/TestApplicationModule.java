package com.huyingbao.rxflux2.module;

import android.app.Application;

import com.huyingbao.rxflux2.inject.module.application.ApplicationModule;

import dagger.Module;

/**
 * Module是一个依赖的制造工厂
 * Created by liujunfeng on 2017/1/1.
 */
@Module
public class TestApplicationModule extends ApplicationModule {
    /**
     * 带有参数的 module 构造方法,必须显式的调用生成实例对象
     *
     * @param application
     */
    public TestApplicationModule(Application application) {
        super(application);
    }
}
