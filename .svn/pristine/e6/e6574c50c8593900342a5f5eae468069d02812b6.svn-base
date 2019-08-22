package com.guitu18.core.interceptor;

import com.guitu18.core.http.Request;

/**
 * 拦截器接口
 *
 * @author zhangkuan
 * @date 2019/8/20
 */
public interface HandlerInterceptor {

    /**
     * 前置拦截器
     *
     * @param request
     * @return
     * @throws Exception
     */
    boolean before(Request request) throws Exception;

    /**
     * 后置拦截器
     *
     * @param request
     * @throws Exception
     */
    void after(Request request) throws Exception;

}
