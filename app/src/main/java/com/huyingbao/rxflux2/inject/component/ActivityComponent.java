package com.huyingbao.rxflux2.inject.component;

import android.app.Activity;

import com.huyingbao.rxflux2.inject.module.ActivityModule;
import com.huyingbao.rxflux2.inject.scope.PerActivity;
import com.huyingbao.simple.MainActivity;

import dagger.Component;

/**
 * activity注入器
 * 不同的Component持有不同的对象，
 * 两个Component间有依赖关系，
 * 那么它们不能使用相同的Scope
 * Created by liujunfeng on 2017/1/1.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class})
public interface ActivityComponent {
    Activity getActivity();

    /**
     * 需要在父Component(ActivityComponent)添加返回子Component(FragmentComponent)的方法
     *
     * @return
     */
    FragmentComponent getFragmentComponent();

    void inject(MainActivity mainActivity);
}
