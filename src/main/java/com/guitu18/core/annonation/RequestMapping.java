package com.guitu18.core.annonation;

import java.lang.annotation.*;

/**
 * RequestMapping
 *
 * @author zhangkuan
 * @date 2019/8/19
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {

    /**
     * Mapping
     *
     * @return
     */
    String value() default "";

}
