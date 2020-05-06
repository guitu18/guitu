package com.guitu18.core.interceptor;

import com.guitu18.core.beans.GuituApplication;
import com.guitu18.core.thread.ThreadLocalRequestHolder;

import java.util.List;

/**
 * InterceptProcess，模仿Spring的InterceptProcess
 *
 * @author zhangkuan
 * @date 2019/8/20
 */
public class InterceptProcess {

    private static InterceptProcess interceptProcess;

    private static List<HandlerInterceptor> interceptors;

    /**
     * 私有构造方法
     */
    private InterceptProcess() {
        // 从IOC容器中获取所有过滤器
        interceptors = GuituApplication.getInstance().getHandlerInterceptor();
    }

    /**
     * 单例实例
     *
     * @return InterceptProcess
     */
    public static InterceptProcess getInstance() {
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
     */
    public boolean processBefore() throws Exception {
        for (HandlerInterceptor interceptor : interceptors) {
            if (!interceptor.before(ThreadLocalRequestHolder.get())) {
                return false;
            }
        }
        return true;
    }

    /**
     * 执行后置拦截器
     */
    public void processAfter() throws Exception {
        for (HandlerInterceptor interceptor : interceptors) {
            interceptor.after(ThreadLocalRequestHolder.get());
        }
    }

}
