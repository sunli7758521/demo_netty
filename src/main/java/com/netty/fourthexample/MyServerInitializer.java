package com.netty.fourthexample;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class MyServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
                            // 空闲检测的一个处理器  责任链模式
        pipeline.addLast(new IdleStateHandler(5,7,10, TimeUnit.SECONDS));
        // 自定义处理器
        pipeline.addLast(new MyServerHandler());
    }
}
