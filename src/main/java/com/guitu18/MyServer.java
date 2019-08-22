package com.guitu18;

import com.guitu18.core.beans.BeanManager;
import com.guitu18.core.bootstrap.NettyBootstrap;

/**
 * 服务启动类
 *
 * @author zhangkuan
 * @date 2019/8/19
 */
public class MyServer {

    /**
     * Main
     *
     * @param args
     */
    public static void main(String[] args) {
        new MyServer().start();
    }

    /**
     * 启动服务
     */
    public void start() {
        BeanManager.getInstance().init();
        NettyBootstrap.start();
    }

}
