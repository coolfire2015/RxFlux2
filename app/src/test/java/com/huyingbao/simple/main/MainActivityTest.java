package com.huyingbao.simple.main;

import android.arch.lifecycle.Lifecycle;

import com.huyingbao.rxflux2.runner.AliRobolectricTestRunner;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.android.controller.ActivityController;

/**
 * Created by liujunfeng on 2017/11/27.
 */
@RunWith(AliRobolectricTestRunner.class)
public class MainActivityTest {
    @Test
    public void afterCreate() throws Exception {
        ActivityController<MainActivity> mainActivityActivityController = Robolectric.buildActivity(MainActivity.class);
        MainActivity activity = mainActivityActivityController.create().get();
        Assert.assertEquals(Lifecycle.State.CREATED, activity.getLifecycle().getCurrentState());
    }

}