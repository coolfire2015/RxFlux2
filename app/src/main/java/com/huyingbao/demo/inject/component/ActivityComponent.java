package com.huyingbao.demo.inject.component;

import android.app.Activity;
import android.content.Context;

import com.huyingbao.demo.inject.module.ActivityModule;
import com.huyingbao.demo.inject.qualifier.ContextLife;
import com.huyingbao.demo.inject.scope.PerActivity;
import com.huyingbao.demo.ui.MainActivity;

import dagger.Component;

/**
 * activity注入器
 * 两个Component间有依赖关系，那么它们不能使用相同的Scope
 * Created by liujunfeng on 2017/1/1.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class})
public interface ActivityComponent {
    /**
     * 通过自定义的@ContextLife区分返回类型相同的@Provides 方法
     *
     * @return
     */
    @ContextLife("Activity")
    Context getActivityContext();

    /**
     * 从对应的ActivityModule中找不到,从dependencies的ApplicationComponent中找得到
     *
     * @return
     */
    @ContextLife("Application")
    Context getApplicationContext();

    Activity getActivity();

    /**
     * 需要在父Component(ActivityComponent)添加返回子Component(FragmentComponent)的方法
     *
     * @return
     */
    FragmentComponent getFragmentComponent();

    void inject(MainActivity mainActivity);
}
