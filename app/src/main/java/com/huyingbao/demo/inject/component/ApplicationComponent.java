package com.huyingbao.demo.inject.component;

import android.content.Context;

import com.hardsoftstudio.rxflux.RxFlux;
import com.huyingbao.demo.action.ActionCreator;
import com.huyingbao.demo.api.HttpApi;
import com.huyingbao.demo.base.application.BaseApplication;
import com.huyingbao.demo.inject.module.application.ApplicationModule;
import com.huyingbao.demo.inject.qualifier.ContextLife;
import com.huyingbao.demo.store.BaseHttpStore;
import com.huyingbao.demo.store.BaseStore;
import com.huyingbao.demo.util.LocalStorageUtils;

import javax.inject.Singleton;

import dagger.Component;

/**
 * application注入器
 * Dagger通过Singleton创建出来的单例并不保持在静态域上，
 * 而是保留在Component实例中接口,自动生成实现
 * Created by liujunfeng on 2017/1/1.
 */
@Singleton
@Component(modules = ApplicationModule.class)//指明Component从ApplicationModule中找依赖
public interface ApplicationComponent {
    /**
     * 1:Component的方法可以没有输入参数，但是就必须有返回值
     * 1.1:返回的实例会先从事先定义的Module(ApplicationModule)中查找，如果找不到跳到Step2
     * 1.2:使用该类带@Inject的构造器来生成返回的实例，并同时也会递归注入构造器参数以及带@Inject的成员变量比如
     *
     * @return
     */
    @ContextLife("Application")
    Context getContext();

    RxFlux getRxFlux();

    ActionCreator getActionCreator();

    LocalStorageUtils getLocalStorageUtils();

    HttpApi getHybApi();

    BaseStore getBaseStore();

    BaseHttpStore getHttpStore();


    /**
     * 2:添加注入方法,一般使用inject做为方法名，方法参数为对应的Container
     * 注入方法，在Container中调用
     * Component的方法输入参数一般只有一个，对应了需要注入的Container
     * 有输入参数返回值类型就是void
     *
     * @param application
     */
    void inject(BaseApplication application);

    void inject(ActionCreator actionCreator);

    void inject(BaseHttpStore baseHttpStore);

    void inject(BaseStore baseStore);
}
