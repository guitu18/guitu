package com.guitu18.core.annonation;

import java.lang.annotation.*;

/**
 * 自动注入注解
 *
 * @author zhangkuan
 * @date 2019/8/21
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {
}
