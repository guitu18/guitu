package com.guitu18.core.bootstrap;

import com.guitu18.common.config.WebConfig;
import com.guitu18.core.annonation.Autowired;
import com.guitu18.core.handler.ChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.log4j.Logger;

/**
 * NettyBootstrap
 *
 * @author zhangkuan
 * @date 2019/8/19
 */
public class NettyBootstrap {

    private static final Logger log = Logger.getLogger(NettyBootstrap.class);

    @Autowired
    private static WebConfig webConfig;

    /**
     * 服务启动
     */
    public static void start() {
        // 用来接收进来的连接
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // 用来处理已经被接收的连接
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    // 这里告诉Channel如何接收新的连接
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    // 添加自己的Handler
                    .childHandler(new ChannelInitializer());

            // 绑定端口，开始接收进来的连接
            ChannelFuture channelFuture = serverBootstrap.bind(webConfig.getServerPort()).sync();
            log.info(">>>>>>>>>> 服务已启动，运行端口：" + webConfig.getServerPort() + " >>>>>>>>>>");

            // 等待服务器socket关闭
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

}
