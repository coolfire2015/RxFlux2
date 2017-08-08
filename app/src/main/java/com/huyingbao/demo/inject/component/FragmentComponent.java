package com.huyingbao.demo.inject.component;

import com.huyingbao.demo.inject.module.FragmentModule;
import com.huyingbao.demo.inject.scope.PerFragment;

import dagger.Subcomponent;

/**
 * fragment注入器
 * 子Component:
 * 注意子Component的Scope范围小于父Component
 * Created by liujunfeng on 2017/1/1.
 */
@PerFragment
@Subcomponent(modules = FragmentModule.class)
public interface FragmentComponent {

}
