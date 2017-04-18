package com.huyingbao.demo.core.inject.component;

import com.huyingbao.demo.core.inject.module.FragmentModule;
import com.huyingbao.demo.core.inject.scope.PerFragment;

import dagger.Subcomponent;

/**
 * fragment注入器
 * 子Component:
 * 注意子Component的Scope范围小于父Component
 * Created by Liu Junfeng on 2017/1/1.
 */
@PerFragment
@Subcomponent(modules = FragmentModule.class)
public interface FragmentComponent {

}
