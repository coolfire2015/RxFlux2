package com.huyingbao.rxflux2.rule;


import com.huyingbao.rxflux2.inject.component.ApplicationComponent;
import com.huyingbao.rxflux2.module.TestApplicationModule;

import org.robolectric.RuntimeEnvironment;

import it.cosenonjaviste.daggermock.DaggerMockRule;

/**
 * Created by liujunfeng on 2017/11/27.
 */

public class AppDaggerMockRule extends DaggerMockRule<ApplicationComponent> {
    public AppDaggerMockRule(Class<ApplicationComponent> componentClass, Object... modules) {
        super(componentClass, new TestApplicationModule(RuntimeEnvironment.application));
    }
}
