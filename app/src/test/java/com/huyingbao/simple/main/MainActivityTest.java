package com.huyingbao.simple.main;

import android.arch.lifecycle.Lifecycle;

import com.huyingbao.rxflux2.base.TestBaseApplication;
import com.huyingbao.rxflux2.base.activity.BaseRxFluxActivity;
import com.huyingbao.rxflux2.inject.component.ActivityComponent;
import com.huyingbao.rxflux2.inject.component.ApplicationComponent;
import com.huyingbao.rxflux2.module.TestActivityModule;
import com.huyingbao.rxflux2.module.TestApplicationModule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import it.cosenonjaviste.daggermock.DaggerMockRule;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by liujunfeng on 2017/11/27.
 */
@RunWith(RobolectricTestRunner.class)
@Config(application = TestBaseApplication.class)
public class MainActivityTest {
    private ActivityController<MainActivity> controller;
    private BaseRxFluxActivity activity;
    private Lifecycle lifecycle;

    public BaseRxFluxActivity getActivity() {
        // 创建Activity控制器
        if (controller == null)
            controller = Robolectric.buildActivity(MainActivity.class);
        // 从控制器获取Activity
        if (activity == null)
            activity = controller.get();
        // 获取Activity的生命周期对象
        if (lifecycle == null)
            lifecycle = activity.getLifecycle();
        return activity;
    }

    @Rule
    public final DaggerMockRule<ActivityComponent> rule =
            new DaggerMockRule<>(ActivityComponent.class, new TestActivityModule(getActivity()))
                    .addComponentDependency(ApplicationComponent.class, new TestApplicationModule(RuntimeEnvironment.application))
                    .set(component -> getActivity().setActivityComponent(component));

    @Test
    public void testActivity() throws Exception {
        controller.create().start().resume();
        assertNotNull(activity);
        assertEquals(activity.getTitle(), "RxFluxDemo");
    }

    @Test
    public void testLifecycle() throws Exception {
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