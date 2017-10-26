package com.huyingbao.rxflux2.inject.scope;

import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * PerService创建出来的单例保留在FragmentComponent实例中
 * Created by liujunfeng on 2017/1/1.
 */
@Scope
@Retention(RUNTIME)
public @interface PerService {
}
