package com.guitu18.core.handler;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * ChannelInitializer
 *
 * @author zhangkuan
 * @date 2019/8/19
 */
public class ChannelInitializer extends io.netty.channel.ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        // SocketChannel代表了一个socket
        // handler分为两种，InboundHandler,OutboundHandler分别处理 流入，流出
        ChannelPipeline pipeline = channel.pipeline();
        // http 编解码
        pipeline.addLast(new HttpServerCodec());
        // http 消息聚合器
        pipeline.addLast("httpAggregator", new HttpObjectAggregator(512 * 1024));
        // 自定义请求处理器
        pipeline.addLast(new DispatcherHandler());
    }

}
