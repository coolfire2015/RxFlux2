package com.huyingbao.rxflux2.inject.component;

import android.content.Context;

import com.huyingbao.rxflux2.inject.module.ServiceModule;
import com.huyingbao.rxflux2.inject.qualifier.ContextLife;
import com.huyingbao.rxflux2.inject.scope.PerService;

import dagger.Component;

/**
 * service注入器
 * Created by liujunfeng on 2017/1/1.
 */
@PerService
@Component(dependencies = ApplicationComponent.class, modules = {ServiceModule.class})
public interface ServiceComponent {

    @ContextLife("Service")
    Context getServiceContext();

    @ContextLife("Application")
    Context getApplicationContext();
}
