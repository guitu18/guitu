package com.guitu18;

import com.guitu18.core.beans.ApplicationContext;
import com.guitu18.core.bootstrap.NettyBootstrap;

/**
 * 服务启动类
 *
 * @author zhangkuan
 * @date 2019/8/19
 */
public class GuituServer {

    /**
     * Main
     */
    public static void main(String[] args) {
        new GuituServer().run();
    }

    /**
     * 启动服务
     */
    public void run() {
        ApplicationContext.getInstance().init();
        NettyBootstrap.start();
    }

}
