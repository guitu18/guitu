package com.guitu18.core.thread;

import com.guitu18.core.http.Request;
import io.netty.util.concurrent.FastThreadLocal;

/**
 * ThreadLocalHolder
 *
 * @author zhangkuan
 * @date 2019/8/20
 */
public class ThreadLocalHolder {

    private static final FastThreadLocal<Request> THREAD_LOCAL = new FastThreadLocal<>();

    /**
     * 保存Request实例
     *
     * @param request
     */
    public static void set(Request request) {
        THREAD_LOCAL.set(request);
    }

    /**
     * 获取Request实例
     *
     * @return
     */
    public static Request get() {
        return THREAD_LOCAL.get();
    }

    /**
     * 移除Request实例
     */
    public static void remove() {
        THREAD_LOCAL.remove();
    }

}
