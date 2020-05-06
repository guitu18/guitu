package com.guitu18.core.annonation;

import java.lang.annotation.*;

/**
 * 自动注入注解，模仿Spring，用法跟Spring一致
 *
 * @author zhangkuan
 * @date 2019/8/21
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {
}
