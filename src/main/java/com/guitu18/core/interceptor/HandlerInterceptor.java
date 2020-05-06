package com.guitu18.core.interceptor;

import com.guitu18.core.http.Request;

/**
 * 拦截器接口，模仿Spring的HandlerInterceptor接口，用法一致
 *
 * @author zhangkuan
 * @date 2019/8/20
 */
public interface HandlerInterceptor {

    /**
     * 前置拦截器
     *
     * @param request Request
     * @return 是否放行
     */
    boolean before(Request request) throws Exception;

    /**
     * 后置拦截器
     *
     * @param request Request
     */
    void after(Request request) throws Exception;

}
