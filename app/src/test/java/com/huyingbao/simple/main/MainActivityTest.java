package com.huyingbao.simple.main;

import android.arch.lifecycle.Lifecycle;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by liujunfeng on 2017/11/27.
 */
@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {
    @Test
    public void testActivity() throws Exception {
        MainActivity sampleActivity = Robolectric.setupActivity(MainActivity.class);
        assertNotNull(sampleActivity);
        assertEquals(sampleActivity.getTitle(), "RxFluxDemo");
    }

    @Test
    public void testLifecycle() throws Exception {
        // 创建Activity控制器
        ActivityController<MainActivity> controller = Robolectric.buildActivity(MainActivity.class);
        // 从控制器获取Activity
        MainActivity activity = controller.get();
        // 获取Activity的生命周期对象
        Lifecycle lifecycle = activity.getLifecycle();
        // 生命周期对象判空
        assertNotNull(lifecycle);

        // 调用Activity的performCreate方法
        controller.create();
        assertEquals(Lifecycle.State.CREATED, lifecycle.getCurrentState());

        // 调用Activity的performStart方法
        controller.start();
        assertEquals(Lifecycle.State.STARTED, lifecycle.getCurrentState());

        // 调用Activity的performResume方法
        controller.resume();
        assertEquals(Lifecycle.State.RESUMED, lifecycle.getCurrentState());

        // 调用Activity的performPause方法
        controller.pause();
        assertEquals(Lifecycle.State.STARTED, lifecycle.getCurrentState());

        // 调用Activity的performStop方法
        controller.stop();
        assertEquals(Lifecycle.State.CREATED, lifecycle.getCurrentState());

        // 调用Activity的performRestart方法
        controller.restart();
        // 注意此处应该是onStart，因为performRestart不仅会调用restart，还会调用onStart
        assertEquals(Lifecycle.State.STARTED, lifecycle.getCurrentState());

        // 调用Activity的performDestroy方法
        controller.destroy();
        assertEquals(Lifecycle.State.DESTROYED, lifecycle.getCurrentState());
    }

}