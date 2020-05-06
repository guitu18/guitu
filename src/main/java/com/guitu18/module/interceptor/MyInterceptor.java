package com.guitu18.module.interceptor;

import com.guitu18.core.http.Request;
import com.guitu18.core.interceptor.HandlerInterceptor;
import org.apache.log4j.Logger;

/**
 * MyInterceptor
 *
 * @author zhangkuan
 * @date 2019/8/20
 */
public class MyInterceptor implements HandlerInterceptor {

    private final Logger log = Logger.getLogger(this.getClass());

    @Override
    public boolean before(Request request) throws Exception {
        log.info(">>>>>>>>>>>>>>> MyInterceptor.before() >>>>>>>>>>>>>>>");
        return true;
    }

    @Override
    public void after(Request request) throws Exception {
        log.info(">>>>>>>>>>>>>>> MyInterceptor.after() >>>>>>>>>>>>>>>");
    }

}
