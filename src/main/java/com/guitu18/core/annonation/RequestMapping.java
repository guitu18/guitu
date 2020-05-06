package com.guitu18.core.annonation;

import java.lang.annotation.*;

/**
 * RequestMapping，模仿Spring，用法跟Spring一致
 *
 * @author zhangkuan
 * @date 2019/8/19
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {

    /**
     * 映射路径
     */
    String value() default "";

}
