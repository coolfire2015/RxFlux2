package com.huyingbao.demo.core.inject.component;

import android.content.Context;

import com.huyingbao.demo.core.inject.module.ServiceModule;
import com.huyingbao.demo.core.inject.qualifier.ContextLife;
import com.huyingbao.demo.core.inject.scope.PerService;

import dagger.Component;

/**
 * service注入器
 * Created by Liu Junfeng on 2017/1/1.
 */
@PerService
@Component(dependencies = ApplicationComponent.class, modules = {ServiceModule.class})
public interface ServiceComponent {

    @ContextLife("Service")
    Context getServiceContext();

    @ContextLife("Application")
    Context getApplicationContext();
}
