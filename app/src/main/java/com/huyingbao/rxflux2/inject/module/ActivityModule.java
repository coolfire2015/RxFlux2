package com.huyingbao.rxflux2.inject.module;

import android.app.Activity;

import com.huyingbao.rxflux2.inject.scope.PerActivity;

import java.lang.ref.WeakReference;

import dagger.Module;
import dagger.Provides;

/**
 * activity中的依赖制造类
 * Module是一个依赖的制造工厂
 * 保存在ActivityComponent
 * Created by liujunfeng on 2017/1/1.
 */
@Module
public class ActivityModule {
    private Activity mActivity;

    public ActivityModule(Activity activity) {
        mActivity = activity;
    }

    @Provides
    @PerActivity
    public Activity provideActivity() {
        //使用弱引用,消除内存泄漏
        return new WeakReference<>(mActivity).get();
    }

    @Provides
    @PerActivity
    public FragmentModule provideFragmentModule() {
        return new FragmentModule();
    }
}
