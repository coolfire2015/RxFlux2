package com.huyingbao.rxflux2.base;

import com.huyingbao.rxflux2.androidtest.inject.component.DaggerTestApplicationComponent;
import com.huyingbao.rxflux2.androidtest.inject.component.TestApplicationComponent;
import com.huyingbao.rxflux2.androidtest.inject.module.TestApplicationModule;
import com.huyingbao.rxflux2.base.application.BaseApplication;
import com.huyingbao.rxflux2.utils.AppUtils;

/**
 * Created by liujunfeng on 2017/1/1.
 */
public class TestBaseApplication extends BaseApplication {
    /**
     * public对外提供方法,重置依赖注入.
     */
    @Override
    public void initDagger() {
        // Module实例的创建
        // 如果Module只有有参构造器,则必须显式传入Module实例,
        // 单例的有效范围随着其依附的Component,
        // 为了使得@Singleton的作用范围是整个Application,需要添加以下代码
        TestApplicationComponent applicationComponent = DaggerTestApplicationComponent.builder()
                .testApplicationModule(new TestApplicationModule(this))
                .build();
        //设置全局静态对象
        AppUtils.setApplicationComponent(applicationComponent);
    }
}