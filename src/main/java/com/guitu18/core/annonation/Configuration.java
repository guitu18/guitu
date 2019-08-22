package com.guitu18.core.annonation;

import java.lang.annotation.*;

/**
 * 配置类注解
 *
 * @author zhangkuan
 * @date 2019/8/21
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Configuration {

    /**
     * 配置前缀，配合字段名即为一个完整的配置
     *
     * @return
     */
    String value();

}
