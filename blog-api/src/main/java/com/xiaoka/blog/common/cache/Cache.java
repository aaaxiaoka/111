package com.xiaoka.blog.common.cache;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
//统一缓存处理
public @interface Cache {
    long expire() default 1 * 60 * 1000;

    String name() default "";
}
