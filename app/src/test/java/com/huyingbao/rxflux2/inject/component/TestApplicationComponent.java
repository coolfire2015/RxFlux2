package com.huyingbao.rxflux2.inject.component;

import com.huyingbao.rxflux2.action.TestActionCreator;
import com.huyingbao.rxflux2.inject.module.TestApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * application注入器 Dagger通过Singleton创建出来的单例并不保持在静态域上， 而是保留在Component实例中接口,自动生成实现
 * Created by liujunfeng on 2017/1/1.
 */
@Singleton
@Component(modules = TestApplicationModule.class) // 指明Component从ApplicationModule中找依赖
public interface TestApplicationComponent extends ApplicationComponent {
    void inject(TestActionCreator testActionCreator);
}
