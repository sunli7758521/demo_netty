package com.netty.thirexample;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class MyServerHandler extends SimpleChannelInboundHandler<String> {
    // 通道广播组
    private static final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
       //自己发送的消息，代表自己
        Channel channel = ctx.channel();
        channelGroup.forEach(ch -> {
            if(channel != ch){
                ch.writeAndFlush(channel.remoteAddress()+"发送消息"+msg+"\n");
            }else {
                ch.writeAndFlush("自己发送消息"+msg+"\n");
            }
        });
    }
//    异常处理机制
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        ctx.close();
    }
//    聊天成广播的形式  相当一个集体的聊天室
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // 获取管道
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[服务器]"+channel.remoteAddress()+"加入\n");
        channelGroup.add(channel);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[服务器]"+channel.remoteAddress()+"离开\n");
        //通道了还有多少了链接
        System.out.println(channelGroup.size());
    }
    // 活跃
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println("[服务器]"+channel.remoteAddress()+"上线\n");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println("[服务器]"+channel.remoteAddress()+"离线\n");
    }


}
