package com.xyauto.assist.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * value 后台创建时展现使用
 * condition 不同策略的条件
 *
 * Created by shiqm on 2017-07-18.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface AutoRelease {
    String value();
}
