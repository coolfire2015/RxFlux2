package com.huyingbao.rxflux2.module;

import android.app.Activity;

import com.huyingbao.rxflux2.inject.module.ActivityModule;

import dagger.Module;

/**
 * Created by liujunfeng on 2017/11/28.
 */
@Module
public class TestActivityModule extends ActivityModule{
    public TestActivityModule(Activity activity) {
        super(activity);
    }
}
