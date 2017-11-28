package com.huyingbao.rxflux2.rule;


import com.huyingbao.rxflux2.inject.component.ActivityComponent;

import it.cosenonjaviste.daggermock.DaggerMockRule;

/**
 * Created by liujunfeng on 2017/11/27.
 */

public class ActivityDaggerMockRule extends DaggerMockRule<ActivityComponent> {
    public ActivityDaggerMockRule(Class<ActivityComponent> componentClass, Object... modules) {
        super(componentClass, modules);

    }
}
