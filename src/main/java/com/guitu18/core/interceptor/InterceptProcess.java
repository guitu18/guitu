package com.guitu18.core.interceptor;

import com.guitu18.core.beans.BeanManager;
import com.guitu18.core.thread.ThreadLocalHolder;

import java.util.List;

/**
 * InterceptProcess
 *
 * @author zhangkuan
 * @date 2019/8/20
 */
public class InterceptProcess {

    private static InterceptProcess interceptProcess;

    private static List<HandlerInterceptor> interceptors;

    private InterceptProcess() {
        interceptors = BeanManager.getInstance().getHandlerInterceptor();
    }

    /**
     * 单例实例
     *
     * @return
     */
    public static InterceptProcess getInstance() {
        interceptProcess = new InterceptProcess();
        if (interceptProcess == null) {
            synchronized (InterceptProcess.class) {
                if (interceptProcess == null) {
                    interceptProcess = new InterceptProcess();
                }
            }
        }
        return interceptProcess;
    }

    /**
     * 执行前置拦截器
     *
     * @return
     * @throws Exception
     */
    public boolean processBefore() throws Exception {
        for (HandlerInterceptor interceptor : interceptors) {
            if (!interceptor.before(ThreadLocalHolder.get())) {
                return false;
            }
        }
        return true;
    }

    /**
     * 执行后置拦截器
     *
     * @throws Exception
     */
    public void processAfter() throws Exception {
        for (HandlerInterceptor interceptor : interceptors) {
            interceptor.after(ThreadLocalHolder.get());
        }
    }

}
