package com.huyingbao.rxflux2.rule;


import com.huyingbao.rxflux2.inject.component.FragmentComponent;

import it.cosenonjaviste.daggermock.DaggerMockRule;

/**
 * Created by liujunfeng on 2017/11/27.
 */

public class FragmentDaggerMockRule extends DaggerMockRule<FragmentComponent> {
    public FragmentDaggerMockRule(Class<FragmentComponent> componentClass, Object... modules) {
        super(componentClass, modules);
    }
}
