package com.huyingbao.rxflux2.runner;

import org.junit.runners.model.InitializationError;
import org.robolectric.RoboSettings;
import org.robolectric.RobolectricTestRunner;

/**
 * Created by liujunfeng on 2017/11/27.
 */

public class AliRobolectricTestRunner extends RobolectricTestRunner {
    public AliRobolectricTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
        // 从源码知道MavenDependencyResolver默认以RoboSettings的repositoryUrl和repositoryId为默认值，因此只需要对RoboSetting进行赋值即可
        RoboSettings.setMavenRepositoryId("alimaven");
        RoboSettings.setMavenRepositoryUrl("http://maven.aliyun.com/nexus/content/groups/public/");
    }
}
