package com.huyingbao.rxflux2.runner;

import android.app.Application;
import android.content.Context;
import android.support.test.runner.AndroidJUnitRunner;

import com.huyingbao.rxflux2.androidtest.base.TestBaseApplication;

/**
 * Created by liujunfeng on 2017/6/26.
 */

public class TestDaggerRunner extends AndroidJUnitRunner {
    @Override
    public Application newApplication(ClassLoader cl, String className, Context context) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        String testApplicationClassName = TestBaseApplication.class.getCanonicalName();
        return super.newApplication(cl, testApplicationClassName, context);
    }
}
