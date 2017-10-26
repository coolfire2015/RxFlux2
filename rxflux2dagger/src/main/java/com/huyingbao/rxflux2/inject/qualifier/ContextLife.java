package com.huyingbao.rxflux2.inject.qualifier;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 自定义注解,用户区分Context
 * Created by liujunfeng on 2017/1/1.
 */
@Qualifier
@Documented
@Retention(RUNTIME)
public @interface ContextLife {
    String value() default "Application";
}
